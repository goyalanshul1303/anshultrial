package com.cartonwale.order.api.dao;

import java.util.List;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.order.api.model.Order;

public interface OrderDao extends GenericDao<Order> {

	List<Order> getAllByConsumer(String consumerId);
	
	List<Order> getAllByProvider(String providerId);
	
	List<Order> getCompletedByProvider(String providerId);
	
	List<Order> getPlacedOrders();

	List<Order> getRequirementsByConsumer(String consumerId);

	List<Order> getRecentOrders(List<String> productIds, String consumerId);

}
