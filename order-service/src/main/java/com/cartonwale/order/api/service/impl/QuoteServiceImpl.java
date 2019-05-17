package com.cartonwale.order.api.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.order.api.dao.QuoteDao;
import com.cartonwale.order.api.exception.DuplicateQuoteException;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.model.OrderStatus;
import com.cartonwale.order.api.model.Quote;
import com.cartonwale.order.api.service.OrderService;
import com.cartonwale.order.api.service.QuoteService;

@Service
public class QuoteServiceImpl extends GenericServiceImpl<Quote> implements QuoteService {

	@Autowired
	private QuoteDao quoteDao;

	@Autowired
	private OrderService orderService;

	@PostConstruct
	void init() {
		init(Quote.class, quoteDao);
	}

	@Override
	public Quote add(Quote quote) {

		if (isQuoteExists(quote))
			throw new DuplicateQuoteException();

		quote.setQuoteDate(Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
		quote.setProviderId(SecurityUtil.getAuthUserDetails().getEntityId());
		return super.add(quote);
	}

	@Override
	public List<Quote> getAllByProvider() {
		return quoteDao.getAllByProvider(SecurityUtil.getAuthUserDetails().getEntityId());
	}

	@Override
	public List<Quote> getAllByOrder(String orderId) {

		Order order = orderService.getById(orderId);
		if (order == null || !order.getConsumerId().equals(SecurityUtil.getAuthUserDetails().getEntityId())) {
			return null;
		}

		return quoteDao.getAllByOrder(orderId);
	}

	@Override
	public Order awardOrder(String quoteId) {

		Quote quote = super.getById(quoteId);
		Order order = orderService.getById(quote.getOrderId());

		order.setProviderId(quote.getProviderId());
		OrderStatus status = OrderStatus.MANUFACTURER_ASSIGNED;
		status.setStatusDate(Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")).getTime());
		order.getStatuses().add(status);
		order.setAwardedQuote(quote);

		return orderService.edit(order);

	}

	private boolean isQuoteExists(Quote quote) {
		return quoteDao.getByOrderIdAndProviderId(quote.getOrderId(),
				SecurityUtil.getAuthUserDetails().getEntityId()) != null ? true : false;
	}

}
