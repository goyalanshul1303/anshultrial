package com.cartonwale.product.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;

@Document(collection = "ProductPrice")
public class ProductPrice extends EntityBase {

	private String productId;
	
	private List<Offer> offers;
	
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

	public List<Offer> getOffers() {
		return offers!= null? offers : new ArrayList<>(1);
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
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
