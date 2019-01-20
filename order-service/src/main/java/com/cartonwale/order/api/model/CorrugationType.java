package com.cartonwale.order.api.model;

public enum CorrugationType {
	
	NARROW(1),
	BROAD(2),
	SLIM(3);
	
	private int value;
	
	private CorrugationType(int value){
		
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static CorrugationType getCorrugationType(int value){
		
		for(CorrugationType type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value");
	}

}
