package com.cartonwale.product.api.service;

import java.util.List;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.product.api.model.Product;

public interface ProductService extends GenericService<Product>{

	Product getById(String consumerId, String id);

	List<Product> getAll(String consumerId);

	List<Product> getProductsAcceptingOffers();

}
