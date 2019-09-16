package com.cartonwale.order.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;

@Document(collection = "Cart")
public class Cart extends EntityBase {

	private String consumerId;
	
	private List<CartItem> items = new ArrayList<CartItem>();

	public Cart(String consumerId) {
		super();
		this.consumerId = consumerId;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String userId) {
		this.consumerId = userId;
	}

	public List<CartItem> getItems() {
		return items;
	}	
		
}
