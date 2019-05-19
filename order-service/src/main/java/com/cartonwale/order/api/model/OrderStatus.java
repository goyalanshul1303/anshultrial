package com.cartonwale.order.api.model;

import java.util.Date;

public class OrderStatus {
	
	public OrderStatus(Status status, Date statusDate) {
		this.status = status;
		this.statusDate = statusDate;
	}

	public enum Status {
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

		protected int value;

		private Status(int value) {

			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
		
		public static Status getStatus(int value) {

			for (Status type : Status.values()) {
				if (type.getValue() == value) {
					return type;
				}
			}

			throw new IllegalArgumentException("Illegal integer value");
		}

	}

	private Date statusDate;
	
	private Status status;

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}
	
	public int getStatus(){
		return status.getValue();
	}

}
