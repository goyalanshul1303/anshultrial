package com.cartonwale.order.api.model;

import com.cartonwale.common.model.EntityBase;
import com.cartonwale.common.model.SheetLayerType;

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
	
}
