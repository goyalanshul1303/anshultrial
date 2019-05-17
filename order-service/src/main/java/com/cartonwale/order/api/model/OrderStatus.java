package com.cartonwale.order.api.model;

import java.util.Date;

public enum OrderStatus {

	ORDER_PLACED(1),
	MANUFACTURER_ASSIGNED(2),
	MANUFACTURING_INITIATED(3),
	MANUFACTURING_COMPLETED(4),
	QUALITY_INSPECTION_INITIATED(5),
	QUALITY_INSPECTION_COMPLETED(6),
	ORDER_DISPATCHED(7),
	ORDER_DELIVERED(8),
	ORDER_COMPLETED(9),
	AWAITING_MANUFACTURER(10);
	
	private int value;
	
	private Date statusDate;
	
	private OrderStatus(int value){
		
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public static OrderStatus getOrderStatus(int value){
		
		for(OrderStatus type : values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		throw new IllegalArgumentException("Illegal integer value");
	}
}
