package com.cartonwale.order.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "Order")
public class Order extends EntityBase{

	private CartonType cartonType;
	
	private CorrugationType corrugationType;
	
	private int quantity;
	
	private PrintingType printingType;
	
	private OrderStatus orderStatus;
	
	private String consumerId;
	
	private String providerId;
	
	private Dimension dimension;

	public CartonType getCartonType() {
		return cartonType;
	}

	public void setCartonType(CartonType cartonType) {
		this.cartonType = cartonType;
	}

	public CorrugationType getCorrugationType() {
		return corrugationType;
	}

	public void setCorrugationType(CorrugationType corrugationType) {
		this.corrugationType = corrugationType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public PrintingType getPrintingType() {
		return printingType;
	}

	public void setPrintingType(PrintingType printingType) {
		this.printingType = printingType;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@JsonIgnore
	public String getConsumerId() {
		return consumerId;
	}

	@JsonIgnore
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	@JsonIgnore
	public String getProviderId() {
		return providerId;
	}

	@JsonIgnore
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
}
