package com.cartonwale.product.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cartonwale.common.model.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Order extends EntityBase{
	
	private int quantity;
	
	private OrderStatus orderStatus;
	
	private String consumerId;
	
	private String providerId;
	
	private Dimension dimension;
	
	private Date orderDate;
	
	private String productId;
	
	private String productName;
	
	private int changeDimension;
	
	private List<OrderStatus> statuses = new ArrayList<>();
	
	private double orderAmount;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOrderStatus() {
		return orderStatus.getValue();
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = OrderStatus.getOrderStatus(orderStatus);
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

	@JsonIgnore
	public List<OrderStatus> getStatuses() {
		return statuses;
	}

	@JsonIgnore
	public void setStatuses(List<OrderStatus> statuses) {
		this.statuses = statuses;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	
}
