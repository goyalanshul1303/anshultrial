package com.app.carton.provider;

/**
 * Created by aggarwal.swati on 4/25/19.
 */

public class AddOfferRequest {
    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }


    public String getPriceOffer() {
        return priceOffer;
    }

    public void setPriceOffer(String priceOffer) {
        this.priceOffer = priceOffer;
    }

    String providerId;

    public String getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(String offerDate) {
        this.offerDate = offerDate;
    }

    String offerDate;
    String priceOffer;
}
