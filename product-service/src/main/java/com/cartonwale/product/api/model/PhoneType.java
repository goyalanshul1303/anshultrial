package com.cartonwale.product.api.model;

public enum PhoneType {

	OFFICE(1),
	MOBILE(2);
	
	private int value;
	
	private PhoneType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static PhoneType getPhoneType(int value){
		
		for(PhoneType type: values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer type");
	}
}
