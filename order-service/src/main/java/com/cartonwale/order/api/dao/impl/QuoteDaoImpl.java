package com.cartonwale.order.api.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.order.api.dao.QuoteDao;
import com.cartonwale.order.api.model.Quote;

@Repository
public class QuoteDaoImpl extends GenericDaoImpl<Quote> implements QuoteDao {

	public QuoteDaoImpl() {
		super(Quote.class);
	}

	@Override
	public List<Quote> getAllByProvider(String providerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("providerId").is(providerId));
			return super.getAll(query);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Quote> getAllByOrder(String orderId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("orderId").is(orderId));
			return super.getAll(query);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Quote getByOrderIdAndProviderId(String orderId, String providerId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("orderId").is(orderId).and("providerId").is(providerId));
			List<Quote> quotes = super.getAll(query);
			return quotes.isEmpty() ? null : quotes.get(0);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
