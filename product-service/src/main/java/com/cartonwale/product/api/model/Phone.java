package com.cartonwale.product.api.model;

public class Phone {

	private PhoneType type;
	private String number;
	
	public PhoneType getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = PhoneType.valueOf(type);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
