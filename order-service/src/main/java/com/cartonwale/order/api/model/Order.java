package com.cartonwale.order.api.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus.getValue();
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
	
}
