package com.cartonwale.consumer.api.model;

public enum AddressType {

	REGISTERED(1),
	CORRESPONDANCE(2);
	
	private int value;
	
	private AddressType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static AddressType getAddressType(int value){
		
		for(AddressType type: values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer type");
	}
}
