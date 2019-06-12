package com.application.onboarding.providersob;

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

    String email;
    String price;
    DimensionClass dimension;
    LastOrderObject lastOrder;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    boolean isSelected ;
    public LastOrderObject getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(LastOrderObject lastOrder) {
        this.lastOrder = lastOrder;
    }


}
