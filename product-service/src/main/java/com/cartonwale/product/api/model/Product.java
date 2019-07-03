package com.cartonwale.product.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;
import com.cartonwale.common.model.SheetLayerType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "Product")
public class Product extends EntityBase{
	
	private String name;

	private CartonType cartonType;
	
	private SheetLayerType sheetLayerType;
	
	private CorrugationType corrugationType;
	
	private int quantity;
	
	private PrintingType printingType;
	
	private String consumerId;
	
	private Dimension dimension;
	
	private Double price;
	
	private Order lastOrder;
	
	private String additionalComments;
	
	private int grammage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CartonType getCartonType() {
		return cartonType;
	}

	public void setCartonType(int value) {
		this.cartonType = CartonType.getCartonType(value);
	}

	public SheetLayerType getSheetLayerType() {
		return sheetLayerType;
	}

	public void setSheetLayerType(int value) {
		this.sheetLayerType = SheetLayerType.getSheetLayerType(value);
	}

	public CorrugationType getCorrugationType() {
		return corrugationType;
	}

	public void setCorrugationType(int value) {
		this.corrugationType = CorrugationType.getCorrugationType(value);
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

	public void setPrintingType(int value) {
		this.printingType = PrintingType.getPrintingType(value);
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@JsonProperty
	public Order getLastOrder() {
		
		return lastOrder;
	}

	@JsonIgnore
	public void setLastOrder(Order lastOrder) {
		this.lastOrder = lastOrder;
	}

	public String getAdditionalComments() {
		return additionalComments;
	}

	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}

	public int getGrammage() {
		return grammage;
	}

	public void setGrammage(int grammage) {
		this.grammage = grammage;
	}
	
}
