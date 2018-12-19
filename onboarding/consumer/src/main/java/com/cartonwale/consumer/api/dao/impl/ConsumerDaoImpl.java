package com.cartonwale.consumer.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.consumer.api.dao.ConsumerDao;
import com.cartonwale.consumer.api.model.Consumer;

@Repository
public class ConsumerDaoImpl extends GenericDaoImpl<Consumer> implements ConsumerDao{
	
	public ConsumerDaoImpl() {
		super(Consumer.class);
	}
}
