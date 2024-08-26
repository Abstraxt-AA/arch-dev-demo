package com.example.demo.stock.controller;

import com.example.demo.stock.proto.RequestWrapper;
import com.example.demo.stock.proto.ResponseWrapper;
import com.example.demo.stock.service.contract.StockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(final StockService stockService) {
        this.stockService = stockService;
    }

    @PutMapping({"/upsert", "/v1/upsert"})
    public Mono<ResponseWrapper> stockUpsert(final @RequestBody Mono<RequestWrapper> request) {
        return stockService.stockUpsert(request)
                .flatMap(val -> Mono.just(ResponseWrapper.newBuilder()
                        .setUpsertStockResponse(val.toStockMessage())
                        .build()));
    }

    @PostMapping({"/reserve", "/v1/reserve"})
    public Mono<ResponseWrapper> reserveOrder(final @RequestBody Mono<RequestWrapper> request) {
        return stockService.reserveOrder(request);
    }
}
