package com.cartonwale.product.api.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.product.api.dao.ProductDao;
import com.cartonwale.product.api.exception.ProductException;
import com.cartonwale.product.api.model.Product;

@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product> implements ProductDao{
	
	public ProductDaoImpl() {
		super(Product.class);
	}

	@Override
	public List<Product> getAllByUser(String userId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("userId").is(userId));
			return super.getAll(query);
		} catch (DataAccessException e) {
			throw new ProductException(e.getMessage());
		}
	}

	@Override
	public Product getById(String userId, String id) {
		try {
			Query query = new Query();
			/*query.addCriteria(Criteria.where("userId").is(userId));*/
			query.addCriteria(Criteria.where("_id").is(id));
			return super.getAll(query).get(0);
		} catch (DataAccessException e) {
			throw new ProductException(e.getMessage());
		}
	}

	@Override
	public List<Product> getByProductIds(List<String> productIds) {
		
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").in(productIds));
			return super.getAll(query);
		} catch (DataAccessException e) {
			throw new ProductException(e.getMessage());
		}
	}

	@Override
	public List<Product> getByName(String name, String userId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(name));
			/*query.addCriteria(Criteria.where("userId").is(userId));*/
			return super.getAll(query);
		} catch (DataAccessException e) {
			throw new ProductException(e.getMessage());
		}
	}
}
