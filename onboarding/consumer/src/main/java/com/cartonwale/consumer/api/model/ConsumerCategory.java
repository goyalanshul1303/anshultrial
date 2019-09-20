package com.cartonwale.consumer.api.model;

public enum ConsumerCategory {

	BOUNDED(1),
	UNBOUNDED(2);
	
	private int value;
	
	private ConsumerCategory(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static ConsumerCategory getConsumerCategory(int value){
		for(ConsumerCategory type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value passed");
	}
}
