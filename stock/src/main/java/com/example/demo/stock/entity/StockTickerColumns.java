package com.example.demo.stock.entity;

public enum StockTickerColumns {
    PRODUCT_ID("product_id"),
    COUNTRY_CODE("country_code"),
    AVAILABLE_STOCK("available_stock");

    public final String value;

    StockTickerColumns(String value) {
        this.value = value;
    }
}