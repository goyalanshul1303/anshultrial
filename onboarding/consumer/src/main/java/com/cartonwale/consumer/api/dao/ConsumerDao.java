package com.cartonwale.consumer.api.dao;

import java.util.List;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.consumer.api.model.Consumer;

public interface ConsumerDao extends GenericDao<Consumer> {

	List<Consumer> getBoundedConsumersByProviderId(String providerId);

}
