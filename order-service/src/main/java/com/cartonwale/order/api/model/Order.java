package com.cartonwale.order.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "Order")
public class Order extends EntityBase{
	
	private int quantity;
	
	private int orderStatus;
	
	private String consumerId;
	
	private String providerId;
	
	private Dimension dimension;
	
	private Date orderDate;
	
	private String productId;
	
	private String productName;
	
	private int changeDimension;
	
	private Quote awardedQuote;
	
	private boolean quotesInvited;
	
	private double orderAmount;
	
	private List<OrderStatus> statuses = new ArrayList<>();
	
	private List<String> items = new ArrayList<>();

	public boolean isQuotesInvited() {
		return quotesInvited;
	}

	public void setQuotesInvited(boolean quotesInvited) {
		this.quotesInvited = quotesInvited;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOrderStatus() {
		return this.orderStatus;
	}

	@JsonIgnore
	public String getConsumerId() {
		return consumerId;
	}

	@JsonIgnore
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	@JsonIgnore
	public String getProviderId() {
		return providerId;
	}

	@JsonIgnore
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getChangeDimension() {
		return changeDimension;
	}

	public void setChangeDimension(int changeDimension) {
		this.changeDimension = changeDimension;
	}

	@JsonProperty
	public Quote getAwardedQuote() {
		return awardedQuote;
	}

	@JsonIgnore
	public void setAwardedQuote(Quote awardedQuote) {
		this.awardedQuote = awardedQuote;
	}

	@JsonProperty
	public List<OrderStatus> getStatuses() {
		return statuses;
	}

	@JsonIgnore
	public void setStatuses(List<OrderStatus> statuses) {
		this.statuses = statuses;
	}

	@JsonProperty
	public double getOrderAmount() {
		return this.orderAmount;
	}

	@JsonIgnore
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	
}
