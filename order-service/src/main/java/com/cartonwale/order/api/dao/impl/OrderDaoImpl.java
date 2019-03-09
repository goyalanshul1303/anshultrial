package com.cartonwale.order.api.dao.impl;

import java.util.List;

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
			query.addCriteria(Criteria.where("consumerId").is(consumerId));
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
}
