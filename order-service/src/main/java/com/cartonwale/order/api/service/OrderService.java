package com.cartonwale.order.api.service;

import java.util.List;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.model.Quote;

public interface OrderService extends GenericService<Order>{

	Order add(Order order, String authToken);
	
	List<Order> getPlacedOrders();

}
