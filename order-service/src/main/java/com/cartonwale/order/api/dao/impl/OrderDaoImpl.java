package com.cartonwale.order.api.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.order.api.dao.OrderDao;
import com.cartonwale.order.api.model.Order;

@Repository
public class OrderDaoImpl extends GenericDaoImpl<Order> implements OrderDao{
	
	public OrderDaoImpl() {
		super(Order.class);
	}

	@Override
	public List<Order> getAllByConsumer(String consumerId) {
		try {
			return super.getAllByColumn("consumerId", consumerId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
