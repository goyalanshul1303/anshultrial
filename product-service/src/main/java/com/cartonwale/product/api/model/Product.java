package com.cartonwale.product.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "Product")
public class Product extends EntityBase {

	private String name;

	private int quantity;

	private String userId;

	private Dimension dimension;

	private Double price;

	private Order lastOrder;

	private String additionalComments;
	
	private List<String> images = new ArrayList<String>();
	
	private ProductCategory category;
	
	private ProductSpecification specifications;
	
	private int cartQuantity;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@JsonProperty
	public Order getLastOrder() {

		return lastOrder;
	}

	@JsonIgnore
	public void setLastOrder(Order lastOrder) {
		this.lastOrder = lastOrder;
	}

	public String getAdditionalComments() {
		return additionalComments;
	}

	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}
	
	public List<String> getImages() {
		return images;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = ProductCategory.getProductCategory(category);
	}

	public ProductSpecification getSpecifications() {
		return specifications;
	}

	public void setSpecifications(ProductSpecification specifications) {
		this.specifications = specifications;
	}

	@JsonProperty
	public int getCartQuantity() {
		return cartQuantity;
	}

	@JsonIgnore
	public void setCartQuantity(int cartQuantity) {
		this.cartQuantity = cartQuantity;
	}

}
