package com.cartonwale.provider.api.model;

public enum OperatingHoursType {
	DAY(1),
	NIGHT(2),
	FULL(3);
	
	private int value;
	
	private OperatingHoursType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static OperatingHoursType getOperatingHoursType(int value) {
		
		for(OperatingHoursType type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Invalid integer value passed");

	}
}
