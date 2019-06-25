package com.cartonwale.order.api.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.dao.SequenceDao;
import com.cartonwale.common.exception.BadRequestException;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.exception.OrderProcessNotFoundException;
import com.cartonwale.common.exception.ProductNotFoundException;
import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.ServiceUtil;
import com.cartonwale.order.api.dao.OrderDao;
import com.cartonwale.order.api.model.ConsumerDashboard;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.model.OrderStatus;
import com.cartonwale.order.api.model.Product;
import com.cartonwale.order.api.model.ProductPrice;
import com.cartonwale.order.api.model.ProviderDashboard;
import com.cartonwale.order.api.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderServiceImpl extends GenericServiceImpl<Order> implements OrderService {

	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	public static final String DUMMY_TOKEN = "dummyToken";

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SequenceDao sequenceDao;

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
			OrderStatus status = new OrderStatus(OrderStatus.Status.ORDER_PLACED, Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
			order.getStatuses().add(status);
			order.setOrderStatus(OrderStatus.Status.ORDER_PLACED.getValue());
		}
		else {
			OrderStatus status = new OrderStatus(OrderStatus.Status.AWAITING_MANUFACTURER, Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
			order.getStatuses().add(status);
			order.setOrderStatus(OrderStatus.Status.AWAITING_MANUFACTURER.getValue());
		}
		
		try {
			order.setId(sequenceDao.getNextSequenceId("order") + "");
			return super.add(order);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
		
		return null;
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
	public List<Order> getAll(String authToken) {

		List<Order> orders = null;
		
		if(SecurityUtil.getAuthUserDetails().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER_ADMIN")))
			try {
				orders = orderDao.getAll();
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
			}
		else
			orders = orderDao.getAllByConsumer(SecurityUtil.getAuthUserDetails().getEntityId());
		calculateOrderAmount(orders, authToken);
		return orders;
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

		if (order.getOrderStatus() >= statusId)
			throw new BadRequestException("Order is currently at stated or forward status");

		OrderStatus status = new OrderStatus(OrderStatus.Status.getStatus(statusId), Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
		order.getStatuses().add(status);
		order.setOrderStatus(statusId);
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
	public List<Order> getRequirementsByConsumer(String authToken) {
		
		List<Order> orders = orderDao.getRequirementsByConsumer(SecurityUtil.getAuthUserDetails().getEntityId());
		
		calculateOrderAmount(orders, authToken);

		return orderDao.getRequirementsByConsumer(SecurityUtil.getAuthUserDetails().getEntityId());
	}

	@Override
	public List<Order> recentOrders(List<String> productIds) {
		
		return orderDao.getRecentOrders(productIds, SecurityUtil.getAuthUserDetails().getEntityId());
	}

	@Override
	public List<Order> getAllToUpdate() {
		
		return orderDao.getAllToBeUpdated();
	}

	@Override
	public ConsumerDashboard getConsumerDashboard() {
		return new ConsumerDashboard(getRequirementsByConsumer(DUMMY_TOKEN).size(), getAll().size());
	}

	@Override
	public ProviderDashboard getProviderDashboard(String authToken) {
		return new ProviderDashboard(getAllByProvider().size(), getPlacedOrders().size(), getProductsForPricing(authToken).size());
	}

	@Override
	public List<Order> getCompletedByConsumer(String authToken) {
		
		List<Order> orders = orderDao.getCompletedByConsumer(SecurityUtil.getAuthUserDetails().getEntityId());
		calculateOrderAmount(orders, authToken);
		return orders;
	}
	
	private List<Product> getProductsForPricing(String authToken) {
		ResponseEntity<List<Product>> responseEntity = ServiceUtil.callByType(HttpMethod.GET, authToken,
				Arrays.asList(MediaType.APPLICATION_JSON), null, "http://PRODUCT-SERVICE/product/acceptingOffers",
				null, restTemplate, new ParameterizedTypeReference<List<Product>>() {
				});
		
		return responseEntity.getBody();
	}
	
	private void calculateOrderAmount(List<Order> orders, String authToken) {
		
		if(DUMMY_TOKEN.equals(authToken))
			return;

		List<String> productIds = orders.stream()
				.filter(o -> !o.isQuotesInvited() || o.getOrderStatus() == OrderStatus.Status.ORDER_PLACED.getValue())
				.map(order -> order.getProductId()).collect(Collectors.toList());

		ResponseEntity<List<ProductPrice>> responseEntity = ServiceUtil.callByType(HttpMethod.PUT, authToken,
				Arrays.asList(MediaType.APPLICATION_JSON), null, "http://PRODUCT-SERVICE/price",
				getProductIdsAsString(productIds), restTemplate, new ParameterizedTypeReference<List<ProductPrice>>() {
				});

		List<ProductPrice> prices = responseEntity.getBody();

		Map<String, ProductPrice> productPriceMap = prices.stream()
				.collect(Collectors.toMap(ProductPrice::getProductId, p -> p));
		orders.stream().forEach(o -> {
			if (!o.isQuotesInvited() || o.getOrderStatus() == OrderStatus.Status.ORDER_PLACED.getValue())
				o.setOrderAmount(productPriceMap.get(o.getProductId()).getPrice() * o.getQuantity());
			else
				o.setOrderAmount(o.getAwardedQuote().getQuoteAmount());
		});

	}
	
	private String getProductIdsAsString(List<String> productIds) {
		ObjectMapper mapper = new ObjectMapper();

		String json = null;
		try {
			json = mapper.writeValueAsString(productIds);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		logger.error(json);
		return json;
	}

	@Override
	public Order getById(String id, String authToken) {
		Order order = super.getById(id);
		List<Order> orders = Arrays.asList(order);
		calculateOrderAmount(orders, authToken);
		return orders.get(0);
	}
}
