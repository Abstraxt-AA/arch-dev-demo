package com.example.demo.checkout.pojo;

public record StockAndStatus(boolean status, long stock) {

    public static final String CHECKOUT_REDIS_KEY = "checkout";

    public StockAndStatus withStatus(boolean status) {
        return new StockAndStatus(status, this.stock);
    }

    public StockAndStatus withStock(long stock) {
        return new StockAndStatus(this.status, stock);
    }
}
