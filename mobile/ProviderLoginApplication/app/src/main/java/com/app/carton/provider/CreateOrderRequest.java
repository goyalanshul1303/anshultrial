package com.app.carton.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggarwal.swati on 12/28/18.
 */

public class CreateOrderRequest {
    List<Integer> cartonType = new ArrayList();
    private int printingType;
    List<Integer> corrugationType = new ArrayList();
    List<Integer> supportedSheetLayers = new ArrayList();
    private int consumerType;
    private int expectedQuantity;


    public int getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(int expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public List<Integer> getCartonType() {
        return cartonType;
    }

    public void setCartonType(List<Integer> cartonType) {
        this.cartonType = cartonType;
    }

    public int getPrintingType() {
        return printingType;
    }

    public void setPrintingType(int printingType) {
        this.printingType = printingType;
    }
    public List<Integer> getCorrugationType() {
        return corrugationType;
    }

    public void setCorrugationType(List<Integer> corrugationType) {
        this.corrugationType = corrugationType;
    }

    public List<Integer> getSupportedSheetLayers() {
        return supportedSheetLayers;
    }

    public void setSupportedSheetLayers(List<Integer> supportedSheetLayers) {
        this.supportedSheetLayers = supportedSheetLayers;
    }
    public int getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(int consumerType) {
        this.consumerType = consumerType;
    }
}
