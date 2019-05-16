package com.app.carton.consumer;

import org.json.JSONObject;

/**
 * Created by aggarwal.swati on 2/11/19.
 */

public class ProductsDetailsItem {
    String id;
    String status;
    String name;
    String cartonType;
    String sheetLayerType;
    String consumerId;
    String quantity;
    String corrugationType;
    String printingType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCartonType() {
        return cartonType;
    }

    public void setCartonType(String cartonType) {
        this.cartonType = cartonType;
    }

    public String getSheetLayerType() {
        return sheetLayerType;
    }

    public void setSheetLayerType(String sheetLayerType) {
        this.sheetLayerType = sheetLayerType;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCorrugationType() {
        return corrugationType;
    }

    public void setCorrugationType(String corrugationType) {
        this.corrugationType = corrugationType;
    }

    public String getPrintingType() {
        return printingType;
    }

    public void setPrintingType(String printingType) {
        this.printingType = printingType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public DimensionClass getDimension() {
        return dimension;
    }

    public void setDimension(DimensionClass dimension) {
        this.dimension = dimension;
    }

    public LastOrderObject getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(LastOrderObject lastOrder) {
        this.lastOrder = lastOrder;
    }

    String price;
    DimensionClass dimension;
    LastOrderObject lastOrder;

}
