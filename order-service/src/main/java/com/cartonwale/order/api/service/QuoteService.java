package com.cartonwale.order.api.service;

import java.util.List;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.model.Quote;

public interface QuoteService extends GenericService<Quote> {
	
	Quote add(Quote quote);

	List<Quote> getAllByProvider();

	List<Quote> getAllByOrder(String orderId);
	
	Order awardOrder(String quoteId);

}
