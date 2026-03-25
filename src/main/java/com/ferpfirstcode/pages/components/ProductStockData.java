package com.ferpfirstcode.pages.components;

public class ProductStockData {

    private String productName;
    private int lastQuantity;

    public ProductStockData(String productName, int lastQuantity) {
        this.productName = productName;
        this.lastQuantity = lastQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getLastQuantity() {
        return lastQuantity;
    }
}
