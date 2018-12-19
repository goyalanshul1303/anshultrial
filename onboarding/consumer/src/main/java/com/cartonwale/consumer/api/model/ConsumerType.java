package com.cartonwale.consumer.api.model;

public enum ConsumerType {

	FMCG(1),
	FOOD(2);
	
	private int value;
	
	private ConsumerType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static ConsumerType getConsumerType(int value){
		for(ConsumerType type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value passed");
	}
}
