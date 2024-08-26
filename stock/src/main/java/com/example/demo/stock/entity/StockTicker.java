package com.example.demo.stock.entity;

import com.example.demo.stock.proto.StockMessage;
import com.example.demo.stock.proto.UpsertStockRequest;
import org.springframework.data.relational.core.mapping.Table;


@Table("ticker")
public record StockTicker(
        Long productId,
        String countryCode,
        Long availableStock
) {

    public static StockTicker fromUpsertStockRequest(final UpsertStockRequest request) {
        return new StockTicker(request.getProductId(), request.getCountryCode(), request.getQuantity());
    }

    public StockTicker withAvailableStock(final Long availableStock) {
        return new StockTicker(this.productId, this.countryCode, availableStock);
    }

    public StockMessage toStockMessage() {
        return StockMessage.newBuilder()
                .setProductId(this.productId)
                .setCountryCode(this.countryCode)
                .setAvailableStock(this.availableStock)
                .build();
    }
}
