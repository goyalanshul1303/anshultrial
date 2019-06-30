package com.cartonwale.order.api.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;

@Document(collection = "Quote")
public class Quote  extends EntityBase {
	
	private String orderId;
	
	private String providerId;
	
	private double quoteAmount;
	
	@Deprecated
	private Date orderFulfillmentDate;
	
	private Date orderStartDate;
	
	private Date quoteDate;
	
	private int noOfDays;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public double getQuoteAmount() {
		return quoteAmount;
	}

	public void setQuoteAmount(double quoteAmount) {
		this.quoteAmount = quoteAmount;
	}

	@Deprecated
	public Date getOrderFulfillmentDate() {
		return orderFulfillmentDate;
	}

	@Deprecated
	public void setOrderFulfillmentDate(Date orderFulfillmentDate) {
		this.orderFulfillmentDate = orderFulfillmentDate;
	}

	public Date getOrderStartDate() {
		return orderStartDate;
	}

	public void setOrderStartDate(Date orderStartDate) {
		this.orderStartDate = orderStartDate;
	}

	public Date getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

}
