package com.cartonwale.order.api.model;

public enum CartonType {

	CORRUGATED(1),
	DIECUT(2);
	
	private int value;
	
	private CartonType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static CartonType getCartonType(int value){
		for(CartonType type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value passed");
	}
}
