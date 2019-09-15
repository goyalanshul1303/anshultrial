package com.cartonwale.order.api.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.order.api.dao.CartDao;
import com.cartonwale.order.api.model.Cart;

public class CartDaoImpl extends GenericDaoImpl<Cart> implements CartDao {

private Logger logger = LoggerFactory.getLogger(CartDaoImpl.class);
	
	public CartDaoImpl() {
		super(Cart.class);
	}
	
	@Override
	public List<Cart> getByUserId(String consumerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("consumerId").is(consumerId));
			return super.getAll(query);
		} catch (DataAccessException e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

}
