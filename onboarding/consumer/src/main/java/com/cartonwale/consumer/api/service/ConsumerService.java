package com.cartonwale.consumer.api.service;

import java.util.List;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.consumer.api.model.Consumer;
import com.cartonwale.consumer.api.model.Product;

public interface ConsumerService extends GenericService<Consumer>{
	
	void addConsumerUser(Consumer consumer, String authToken);

	List<Product> getProducts(String token);

	List<Consumer> getBoundedConsumersByProviderId(String providerId);
}
