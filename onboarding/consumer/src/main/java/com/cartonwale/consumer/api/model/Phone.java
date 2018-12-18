package com.cartonwale.consumer.api.model;

public class Phone {

	private PhoneType type;
	private String number;
	
	public PhoneType getType() {
		return type;
	}
	public void setType(int type) {
		this.type = PhoneType.getPhoneType(type);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
