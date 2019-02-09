package com.cartonwale.product.api.service;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.product.api.model.Product;

public interface ProductService extends GenericService<Product>{

	Product getById(String consumerId, String id);

}
