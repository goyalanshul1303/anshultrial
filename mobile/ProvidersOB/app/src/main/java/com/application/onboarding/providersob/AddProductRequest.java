package com.application.onboarding.providersob;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggarwal.swati on 12/20/18.
 */

public class AddProductRequest {


    int cartonType = -1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
    String consumerId ;
    int printingType;

    public DimensionClass getDimension() {
        return dimension;
    }

    public void setDimension(DimensionClass dimension) {
        this.dimension = dimension;
    }

    DimensionClass dimension;

    public int getCorrugationType() {
        return corrugationType;
    }

    public void setCorrugationType(int corrugationType) {
        this.corrugationType = corrugationType;
    }

    int corrugationType = -1;

    public int getCartonType() {
        return cartonType;
    }

    public void setCartonType(int cartonType) {
        this.cartonType = cartonType;
    }


    public int getSheetLayerType() {
        return sheetLayerType;
    }

    public void setSheetLayerType(int sheetLayerType) {
        this.sheetLayerType = sheetLayerType;
    }

    int sheetLayerType  =-1;

    int quantity;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

//    public JSONObject getDimension() {
//        return dimension;
//    }
//
//    public void setDimension(JSONObject dimension) {
//        this.dimension = dimension;
//    }
//
//    JSONObject dimension = new JSONObject();


    public int getPrintingType() {
        return printingType;
    }

    public void setPrintingType(int printingType) {
        this.printingType = printingType;
    }


}
