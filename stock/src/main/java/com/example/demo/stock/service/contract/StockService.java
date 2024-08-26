package com.example.demo.stock.service.contract;

import com.example.demo.stock.entity.StockTicker;
import com.example.demo.stock.proto.RequestWrapper;
import com.example.demo.stock.proto.ResponseWrapper;
import reactor.core.publisher.Mono;

public interface StockService {

    Mono<StockTicker> stockUpsert(Mono<RequestWrapper> request);

    Mono<ResponseWrapper> reserveOrder(Mono<RequestWrapper> request);
}
