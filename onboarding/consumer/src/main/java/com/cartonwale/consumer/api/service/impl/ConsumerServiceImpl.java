package com.cartonwale.consumer.api.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.consumer.api.dao.ConsumerDao;
import com.cartonwale.consumer.api.model.Consumer;
import com.cartonwale.consumer.api.service.ConsumerService;

@Service
public class ConsumerServiceImpl extends GenericServiceImpl<Consumer> implements ConsumerService {
	
	@Autowired
	private ConsumerDao consumerDao;
	
	@PostConstruct
	void init() {
		init(Consumer.class, consumerDao);
	}
	
	@Override
	public Consumer add(Consumer consumer) {
		
		return super.add(consumer);
	}
	
	@Override
	public Consumer edit(Consumer consumer) {
		
		return super.edit(consumer);
	}
	
}
