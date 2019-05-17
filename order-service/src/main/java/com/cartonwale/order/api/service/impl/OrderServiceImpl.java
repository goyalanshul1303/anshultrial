package com.cartonwale.order.api.service.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.exception.BadRequestException;
import com.cartonwale.common.exception.OrderProcessNotFoundException;
import com.cartonwale.common.exception.ProductNotFoundException;
import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.ServiceUtil;
import com.cartonwale.order.api.dao.OrderDao;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.model.OrderStatus;
import com.cartonwale.order.api.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderServiceImpl extends GenericServiceImpl<Order> implements OrderService {

	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private RestTemplate restTemplate;

	@PostConstruct
	void init() {
		init(Order.class, orderDao);
	}

	@Override
	public Order add(Order order, String authToken) {

		if (StringUtils.isEmpty(order.getProductId())) {
			throw new BadRequestException("Product Id is missing");
		}

		checkIfProductExists(order, authToken);

		order.setConsumerId(SecurityUtil.getAuthUserDetails().getEntityId());
		order.setOrderDate(Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
		if(order.isQuotesInvited()){
			OrderStatus status = OrderStatus.ORDER_PLACED;
			status.setStatusDate(Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
			order.getStatuses().add(status);
		}
		else {
			OrderStatus status = OrderStatus.AWAITING_MANUFACTURER;
			status.setStatusDate(Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
			order.getStatuses().add(status);
		}
		return super.add(order);
	}

	private void checkIfProductExists(Order order, String authToken) {

		ResponseEntity<String> responseEntity = ServiceUtil.call(HttpMethod.GET,
				authToken, null, null, "http://PRODUCT-SERVICE/product/"
						+ SecurityUtil.getAuthUserDetails().getEntityId() + "/" + order.getProductId(),
				null, restTemplate);

		String jsonProductDetails = responseEntity.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root;
		try {
			root = objectMapper.readTree(jsonProductDetails);
			String productId = root.get("id").asText();

			if (StringUtils.isEmpty(productId))
				throw new ProductNotFoundException();

			order.setProductName(root.get("name").asText());

		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public Order edit(Order order) {

		return super.edit(order);
	}

	@Override
	public List<Order> getAll() {

		return orderDao.getAllByConsumer(SecurityUtil.getAuthUserDetails().getEntityId());
	}

	@Override
	public List<Order> getPlacedOrders() {

		return orderDao.getPlacedOrders();
	}

	@Override
	public Order changeStatus(String orderId, int statusId) {
		Order order = super.getById(orderId);

		if (order == null) {
			throw new OrderProcessNotFoundException();
		}

		int statusSize = order.getStatuses().size();

		if (order.getStatuses().get(statusSize-1).getValue() == statusId)
			throw new BadRequestException("Order is currently at stated status only");

		OrderStatus status = OrderStatus.getOrderStatus(statusId);
		status.setStatusDate(Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
		order.getStatuses().add(status);
		super.edit(order);
		return order;
	}

	@Override
	public List<Order> getAllByProvider() {

		return orderDao.getAllByProvider(SecurityUtil.getAuthUserDetails().getEntityId());
	}

	@Override
	public List<Order> getCompletedByProvider() {

		return orderDao.getCompletedByProvider(SecurityUtil.getAuthUserDetails().getEntityId());
	}

	@Override
	public List<Order> getRequirementsByConsumer() {

		return orderDao.getRequirementsByConsumer(SecurityUtil.getAuthUserDetails().getEntityId());
	}

	@Override
	public List<Order> recentOrders(List<String> productIds) {
		
		return orderDao.getRecentOrders(productIds, SecurityUtil.getAuthUserDetails().getEntityId());
	}
}
