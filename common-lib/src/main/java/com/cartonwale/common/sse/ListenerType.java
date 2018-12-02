package com.cartonwale.common.sse;

public enum ListenerType {
	
	USER,
	VEHICLE;
	
	public String prepareKey(String id){
		return this.name() + "-" + id;
	}
}
