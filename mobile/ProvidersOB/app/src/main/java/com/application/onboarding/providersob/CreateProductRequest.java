package com.application.onboarding.providersob;

import com.google.gson.JsonObject;

public class CreateProductRequest {
    String name;
    String providerId;
    String consumerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

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

    public DimensionClass getDimension() {
        return dimension;
    }

    public void setDimension(DimensionClass dimension) {
        this.dimension = dimension;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    int quantity;
    DimensionClass dimension;
    int productType;
    int category;

    public JsonObject getSpecifications() {
        return specifications;
    }

    public void setSpecifications(JsonObject specifications) {
        this.specifications = specifications;
    }

    JsonObject specifications = new JsonObject();

}
