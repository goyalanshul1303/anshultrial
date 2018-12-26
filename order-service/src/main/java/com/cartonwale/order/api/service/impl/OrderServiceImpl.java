package com.cartonwale.order.api.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.order.api.dao.OrderDao;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.service.OrderService;

@Service
public class OrderServiceImpl extends GenericServiceImpl<Order> implements OrderService {
	
	@Autowired
	private OrderDao orderDao;
	
	@PostConstruct
	void init() {
		init(Order.class, orderDao);
	}
	
	@Override
	public Order add(Order order) {
		
		return super.add(order);
	}
	
	@Override
	public Order edit(Order order) {
		
		return super.edit(order);
	}
	
	@Override
	public List<Order> getAllByConsumer(String consumerId){
		
		return orderDao.getAllByConsumer(consumerId);
	}
	
}
