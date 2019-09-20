package com.cartonwale.consumer.api.model;

import java.util.ArrayList;
import java.util.List;

import com.cartonwale.common.model.EntityBase;

public class Product extends EntityBase {

	private String name;

	private int quantity;

	private String userId;

	private Double price;

	private String additionalComments;
	
	private List<String> images = new ArrayList<String>();
	
	private ProductCategory category;
	
	private ProductSpecification specifications;

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

	public String getConsumerId() {
		return userId;
	}

	public void setConsumerId(String consumerId) {
		this.userId = consumerId;
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

}
