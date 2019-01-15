package com.cartonwale.consumer.api.service;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.consumer.api.model.Consumer;

public interface ConsumerService extends GenericService<Consumer>{
	
	void addConsumerUser(Consumer consumer, String authToken);
}
