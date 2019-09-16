package com.cartonwale.order.api.dao;

import java.util.List;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.order.api.model.Cart;

public interface CartDao extends GenericDao<Cart>  {

	List<Cart> getByUserId(String consumerId);
}
