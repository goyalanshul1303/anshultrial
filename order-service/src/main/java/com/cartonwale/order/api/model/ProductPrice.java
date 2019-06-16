package com.cartonwale.order.api.model;

import java.util.Date;

import com.cartonwale.common.model.EntityBase;

public class ProductPrice extends EntityBase {

	private String productId;
	
	private double price;
	
	private boolean isAcceptingOffers;
	
	private Date lastUpdated;
	
	public ProductPrice(){
		super();
	}
	
	public ProductPrice(String productId){
		super();
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isAcceptingOffers() {
		return isAcceptingOffers;
	}

	public void setAcceptingOffers(boolean isAcceptingOffers) {
		this.isAcceptingOffers = isAcceptingOffers;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
}
