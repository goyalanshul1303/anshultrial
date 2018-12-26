package com.cartonwale.order.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.order.api.dao.OrderDao;
import com.cartonwale.order.api.model.Order;

@Repository
public class OrderDaoImpl extends GenericDaoImpl<Order> implements OrderDao{
	
	public OrderDaoImpl() {
		super(Order.class);
	}
}
