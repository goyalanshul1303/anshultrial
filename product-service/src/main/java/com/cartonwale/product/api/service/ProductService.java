package com.cartonwale.product.api.service;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.product.api.model.Product;
import com.cartonwale.product.api.model.ProductImageDto;

public interface ProductService extends GenericService<Product>{

	Product getById(String consumerId, String id);

	List<Product> getAllByConsumer(String consumerId);
	
	List<Product> getAll(String authToken);

	List<Product> getProductsAcceptingOffers();

	Boolean uploadProductImage(ProductImageDto imageDto, String id);

	StreamingResponseBody getProductImage(String id);

}
