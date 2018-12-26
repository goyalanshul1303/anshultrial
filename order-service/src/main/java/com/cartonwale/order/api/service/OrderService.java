package com.cartonwale.order.api.service;

import java.util.List;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.order.api.model.Order;

public interface OrderService extends GenericService<Order>{

	List<Order> getAllByConsumer(String consumer);
}
