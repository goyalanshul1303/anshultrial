package com.cartonwale.product.api.model;

public enum PrintingType {

	SINGLE(1),
	DOUBLE(2),
	TRIPLE(3),
	FOUR(4);
	
	private int value;
	
	private PrintingType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static PrintingType getPrintingType(int value){
		
		for(PrintingType type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value passed");
	}
}
