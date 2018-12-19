package com.cartonwale.consumer.api.model;

public enum Frequency {

	MONTHLY(1),
	QUARTERLY(2),
	HALFYEARLY(3),
	YEARLY(4);
	
	private int value;
	
	private Frequency(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static Frequency getFrequency(int value){
		for(Frequency type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value passed");
	}
}
