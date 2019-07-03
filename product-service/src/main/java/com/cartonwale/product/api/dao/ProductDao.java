package com.cartonwale.product.api.dao;

import java.util.List;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.product.api.model.Product;

public interface ProductDao extends GenericDao<Product> {

	List<Product> getAllByConsumer(String consumerId);

	Product getById(String entityId, String id);

	List<Product> getByProductIds(List<String> productIds);
	
	List<Product> getByName(String name, String string);

}
