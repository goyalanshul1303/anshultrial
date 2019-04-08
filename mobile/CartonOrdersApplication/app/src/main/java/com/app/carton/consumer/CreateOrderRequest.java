package com.app.carton.consumer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by aggarwal.swati on 12/28/18.
 */

public class CreateOrderRequest {
  String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DimensionClass getDimension() {
        return dimension;
    }

    public void setDimension(DimensionClass dimension) {
        this.dimension = dimension;
    }

    int quantity;
    DimensionClass dimension;
}
