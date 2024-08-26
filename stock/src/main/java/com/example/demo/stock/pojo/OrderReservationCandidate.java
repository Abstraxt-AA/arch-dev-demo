package com.example.demo.stock.pojo;

import com.example.demo.stock.entity.StockTicker;
import com.example.demo.stock.proto.ReserveItem;

public record OrderReservationCandidate(StockTicker ticker, ReserveItem item) {
}
