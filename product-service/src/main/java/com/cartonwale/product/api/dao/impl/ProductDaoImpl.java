package com.cartonwale.product.api.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.product.api.dao.ProductDao;
import com.cartonwale.product.api.model.Product;

@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product> implements ProductDao{
	
	public ProductDaoImpl() {
		super(Product.class);
	}

	@Override
	public List<Product> getAllByConsumer(String consumerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("consumerId").is(consumerId));
			return super.getAll(query);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Product getById(String consumerId, String id) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("consumerId").is(consumerId));
			query.addCriteria(Criteria.where("_id").is(id));
			return super.getAll(query).get(0);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
