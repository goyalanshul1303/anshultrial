package com.app.carton.provider;


/**
 * Created by aggarwal.swati on 12/20/18.
 */

public class AddQuotationRequest {



    String orderId ;
    String providerId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public int getQuoteAmount() {
        return quoteAmount;
    }

    public void setQuoteAmount(int quoteAmount) {
        this.quoteAmount = quoteAmount;
    }

    public String getOrderFulfillmentDate() {
        return orderFulfillmentDate;
    }

    public void setOrderFulfillmentDate(String orderFulfillmentDate) {
        this.orderFulfillmentDate = orderFulfillmentDate;
    }

    public String getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(String orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    int quoteAmount;
String orderFulfillmentDate;
String orderStartDate;

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    int noOfDays;


}
