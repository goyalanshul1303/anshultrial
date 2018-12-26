package com.cartonwale.order.api.dao;

import java.util.List;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.order.api.model.Order;

public interface OrderDao extends GenericDao<Order> {

	List<Order> getAllByConsumer(String consumerId);

}
