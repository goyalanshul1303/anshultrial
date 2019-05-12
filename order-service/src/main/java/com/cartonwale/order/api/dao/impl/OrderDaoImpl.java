package com.cartonwale.order.api.dao.impl;

import java.util.List;

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

@Repository
public class OrderDaoImpl extends GenericDaoImpl<Order> implements OrderDao{
	
	public OrderDaoImpl() {
		super(Order.class);
	}

	@Override
	public List<Order> getAllByConsumer(String consumerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("consumerId").is(consumerId).and("orderStatus").gt(OrderStatus.ORDER_PLACED.getValue()).lt(OrderStatus.ORDER_COMPLETED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Order> getRequirementsByConsumer(String consumerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("consumerId").is(consumerId).and("orderStatus").is(OrderStatus.ORDER_PLACED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Order> getPlacedOrders() {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("orderStatus").is(OrderStatus.ORDER_PLACED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Order> getAllByProvider(String providerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("providerId").is(providerId).and("orderStatus").gt(OrderStatus.ORDER_PLACED.getValue()).lt(OrderStatus.ORDER_COMPLETED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Order> getCompletedByProvider(String providerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("providerId").is(providerId).and("orderStatus").is(OrderStatus.ORDER_COMPLETED.getValue()));
			return super.getAll(query);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Order> getRecentOrders(List<String> productIds, String consumerId) {
		try {
			
			Aggregation agg = Aggregation.newAggregation(Aggregation.match(Criteria.where("consumerId").is(consumerId).and("productId").in(productIds))
					, Aggregation.sort(Direction.DESC, "productId", "orderDate")
					, Aggregation.group("consumerId", "productId").first("orderDate").as("orderDate")
					, Aggregation.project("orderStatus","quantity").and("orderDate").previousOperation());
			List<Order> orders = super.getAll(agg);
			return orders;
		} catch (DataAccessException e) {
			
			e.printStackTrace();
		}
		return null;
	}
}
