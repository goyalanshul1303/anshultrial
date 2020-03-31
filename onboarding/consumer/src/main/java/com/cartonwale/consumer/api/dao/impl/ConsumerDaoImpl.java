package com.cartonwale.consumer.api.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.consumer.api.dao.ConsumerDao;
import com.cartonwale.consumer.api.exception.ConsumerException;
import com.cartonwale.consumer.api.model.Consumer;

@Repository
public class ConsumerDaoImpl extends GenericDaoImpl<Consumer> implements ConsumerDao{
	
	private Logger logger = LoggerFactory.getLogger(ConsumerDaoImpl.class);
	
	public ConsumerDaoImpl() {
		super(Consumer.class);
	}

	@Override
	public List<Consumer> getBoundedConsumersByProviderId(String providerId) {
		
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("boundedProviderId").is(providerId));
			return super.getAll(query);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new ConsumerException(e.getMessage());
		}
	}
}
