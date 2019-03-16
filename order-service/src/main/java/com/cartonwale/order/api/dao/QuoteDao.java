package com.cartonwale.order.api.dao;

import java.util.List;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.order.api.model.Quote;

public interface QuoteDao extends GenericDao<Quote> {

	List<Quote> getAllByProvider(String providerId);

	List<Quote> getAllByOrder(String orderId);
	
	Quote getByOrderIdAndProviderId(String orderId, String providerId);
}
