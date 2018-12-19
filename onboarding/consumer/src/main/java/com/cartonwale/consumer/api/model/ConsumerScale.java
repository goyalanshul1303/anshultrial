package com.cartonwale.consumer.api.model;

public enum ConsumerScale {

	SMALL(1),
	MEDIUM(2),
	LARGE(3),
	MICRO(4);
	
	private int value;
	
	private ConsumerScale(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static ConsumerScale getConsumerScale(int value){
		for(ConsumerScale type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value passed");
	}
}
