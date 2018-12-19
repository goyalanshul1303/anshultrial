package com.cartonwale.provider.api.model;

public enum SheetLayerType {

	THREE_PLY(3),
	FIVE_PLY(5),
	SEVEN_PLY(7);
	
	private int value;
	
	private SheetLayerType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static SheetLayerType getSheetLayerType(int value){
		
		for(SheetLayerType type: values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer type");
	}
}
