package com.cartonwale.order.api.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.order.api.dao.OrderDao;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.model.OrderStatus;
import com.cartonwale.order.api.service.impl.OrderServiceImpl;

@Repository
public class OrderDaoImpl extends GenericDaoImpl<Order> implements OrderDao{
	
	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	public OrderDaoImpl() {
		super(Order.class);
	}

	@Override
	public List<Order> getAllByConsumer(String consumerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("consumerId").is(consumerId).and("orderStatus").gt(OrderStatus.Status.ORDER_PLACED.getValue()).lt(OrderStatus.Status.ORDER_COMPLETED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}
	
	@Override
	public List<Order> getRequirementsByConsumer(String consumerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("consumerId").is(consumerId).and("orderStatus").is(OrderStatus.Status.ORDER_PLACED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

	@Override
	public List<Order> getPlacedOrders() {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("orderStatus").in(OrderStatus.Status.ORDER_PLACED.getValue(), OrderStatus.Status.AWAITING_MANUFACTURER.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			logger.error(e.getStackTrace().toString());
		}
		
		return null;
	}

	@Override
	public List<Order> getAllByProvider(String providerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("providerId").is(providerId).and("orderStatus").gt(OrderStatus.Status.ORDER_PLACED.getValue()).lt(OrderStatus.Status.ORDER_COMPLETED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

	@Override
	public List<Order> getCompletedByProvider(String providerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("providerId").is(providerId).and("orderStatus").is(OrderStatus.Status.ORDER_COMPLETED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

	@Override
	public List<Order> getRecentOrders(List<String> productIds, String consumerId) {
		try {
			
			Aggregation agg = Aggregation.newAggregation(Aggregation.match(Criteria.where("consumerId").is(consumerId).and("productId").in(productIds))
					, Aggregation.sort(Direction.DESC, "productId", "orderDate")
					, Aggregation.group("productId").first("orderDate").as("orderDate").first(Aggregation.ROOT).as("doc")			
					, Aggregation.replaceRoot("doc"));
			List<Order> orders = super.getAll(agg);
			
			return orders;
		} catch (DataAccessException e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

	@Override
	public List<Order> getAllToBeUpdated() {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("orderStatus").gte(OrderStatus.Status.MANUFACTURING_COMPLETED.getValue()).lt(OrderStatus.Status.ORDER_COMPLETED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}
}
