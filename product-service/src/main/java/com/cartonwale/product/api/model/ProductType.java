package com.cartonwale.product.api.model;

public enum ProductType {

	CORRUGATED_CARTON(1),
	TAPE(2),
	ALUMINIUM_CONTAINER(3);
	
	private int value;
	
	private ProductType(int value){
		
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static ProductType getProductType(int value){
		
		for(ProductType type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value");
	}
}
