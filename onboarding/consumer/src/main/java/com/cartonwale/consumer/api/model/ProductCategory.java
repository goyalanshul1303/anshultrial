package com.cartonwale.consumer.api.model;

public enum ProductCategory {

	CONSUMER(1),
	PROVIDER(2);
	
	private int value;
	
	private ProductCategory(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static ProductCategory getProductCategory(int value){
		for(ProductCategory type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value passed");
	}
}
