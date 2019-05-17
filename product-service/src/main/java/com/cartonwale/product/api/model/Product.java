package com.cartonwale.product.api.model;

import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;
import com.cartonwale.common.model.SheetLayerType;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
		return 100.0;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Order getLastOrder() {
		
		if (this.getId().equals("5cd6bfebad0cf20001abcfef")) {
			lastOrder = new Order();
			lastOrder.setId("5cd79d5ead0cf2000108f136");
			lastOrder.getStatuses().add(OrderStatus.ORDER_COMPLETED);
			lastOrder.setQuantity(200);
			Calendar cal = Calendar.getInstance();
			cal.set(2019, 3, 21);
			lastOrder.setOrderDate(cal.getTime());
		} else if (this.getId().equals("5cd6bf28ad0cf20001abcfed")) {
			lastOrder = new Order();
			lastOrder.setId("5ccde9309943f200016d31dc");
			lastOrder.getStatuses().add(OrderStatus.MANUFACTURING_INITIATED);
			lastOrder.setQuantity(300);
			Calendar cal = Calendar.getInstance();
			cal.set(2019, 4, 28);
			lastOrder.setOrderDate(cal.getTime());
		} else if (this.getId().equals("5c6f02eaa675f200016db0a6")) {
			lastOrder = new Order();
			lastOrder.setId("5cb08bd39943f200019ae959");
			lastOrder.getStatuses().add(OrderStatus.ORDER_PLACED);
			lastOrder.setQuantity(300);
			Calendar cal = Calendar.getInstance();
			cal.set(2019, 4, 28);
			lastOrder.setOrderDate(cal.getTime());
		}
		
		return lastOrder;
	}

	public void setLastOrder(Order lastOrder) {
		this.lastOrder = lastOrder;
	}
	
}
