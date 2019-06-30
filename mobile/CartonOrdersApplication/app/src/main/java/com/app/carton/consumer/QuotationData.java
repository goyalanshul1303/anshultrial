package com.app.carton.consumer;

/**
 * Created by aggarwal.swati on 3/29/19.
 */

public class QuotationData {
    String orderId ;
    String providerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id ;

    public boolean isAwarded() {
        return awarded;
    }

    private boolean awarded = false;

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

    public long getOrderFulfillmentDate() {
        return orderFulfillmentDate;
    }

    public void setOrderFulfillmentDate(long orderFulfillmentDate) {
        this.orderFulfillmentDate = orderFulfillmentDate;
    }

    public long getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(long orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    int quoteAmount;
    long orderFulfillmentDate;
    long orderStartDate;

    public long getQouteDate() {
        return quoteDate;
    }

    public void setQouteDate(long qouteDate) {
        this.quoteDate = qouteDate;
    }

    long quoteDate;

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    int noOfDays;

    public void setAwarded(boolean awarded) {
        this.awarded = awarded;
    }
}
