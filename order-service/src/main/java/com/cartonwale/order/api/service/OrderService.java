package com.cartonwale.order.api.service;

import java.util.List;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.order.api.model.Order;

public interface OrderService extends GenericService<Order>{

	Order add(Order order, String authToken);
	
	List<Order> getPlacedOrders();
	
	Order changeStatus(String orderId, int statusId);
	
	List<Order> getAllByProvider();
	
	List<Order> getCompletedByProvider();
	
	List<Order> getRequirementsByConsumer();

	List<Order> recentOrders(List<String> productIds);

	List<Order> getAllToUpdate();

}
