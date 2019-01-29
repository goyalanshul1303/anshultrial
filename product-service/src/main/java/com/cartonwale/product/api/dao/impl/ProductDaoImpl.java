package com.cartonwale.product.api.dao.impl;

import java.util.List;

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
			return super.getAllByColumn("consumerId", consumerId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
