package com.example.demo.stock.service.impl;

import com.example.demo.stock.entity.StockTicker;
import com.example.demo.stock.entity.StockTickerColumns;
import com.example.demo.stock.pojo.OrderReservationCandidate;
import com.example.demo.stock.proto.RequestWrapper;
import com.example.demo.stock.proto.ReserveItem;
import com.example.demo.stock.proto.ReserveOrderResponse;
import com.example.demo.stock.proto.ResponseWrapper;
import com.example.demo.stock.service.contract.StockService;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class StockServiceImpl implements StockService {

    private final R2dbcEntityTemplate template;
    private final DatabaseClient dbClient;


    public StockServiceImpl(final R2dbcEntityTemplate template, final DatabaseClient dbClient) {
        this.template = template;
        this.dbClient = dbClient;
    }

    @Override
    @Transactional
    public Mono<StockTicker> stockUpsert(final Mono<RequestWrapper> request) {
        return request.flatMap(val -> Mono.just(StockTicker.fromUpsertStockRequest(val.getUpsertStockRequest())))
                .flatMap(this::upsert);
    }

    @Override
    public Mono<ResponseWrapper> reserveOrder(final Mono<RequestWrapper> request) {
        final var candidates = request
                .flatMapMany(val -> Flux.fromIterable(val.getReserveOrderRequest().getItemsList()))
                .flatMap(this::fetchOrderCandidates)
                .cache();
        final var success = new AtomicBoolean(true);
        return candidates.any(val -> val.ticker().availableStock() < 0)
                .flatMapMany(failed -> {
                    if (Boolean.TRUE.equals(failed)) {
                        success.setPlain(false);
                        return failedOrderFlux(candidates);
                    }
                    return candidates.flatMap(candidate -> Mono.just(candidate.ticker()).flatMap(this::upsert));
                })
                .flatMap(ticker -> Mono.just(ticker.toStockMessage()))
                .collectList()
                .flatMap(stockMessages -> Mono.just(ResponseWrapper.newBuilder()
                        .setReserveOrderResponse(ReserveOrderResponse.newBuilder()
                                .setSuccess(success.getPlain())
                                .addAllStocks(stockMessages)
                                .build()).build()));
    }

    private Flux<StockTicker> failedOrderFlux(final Flux<OrderReservationCandidate> candidates) {
        return candidates
                .flatMap(candidate -> Mono.just(
                        candidate.ticker().withAvailableStock(
                                candidate.ticker().availableStock() + candidate.item().getQuantity()
                        )
                ));
    }

    private Mono<StockTicker> upsert(final StockTicker ticker) {
        return dbClient.sql("""
                        INSERT INTO stock.ticker (product_id, country_code, available_stock)
                            VALUE (:product_id, :country_code, :available_stock)
                        ON DUPLICATE KEY
                            UPDATE available_stock = :available_stock
                        """)
                .bind(StockTickerColumns.PRODUCT_ID.value, ticker.productId())
                .bind(StockTickerColumns.COUNTRY_CODE.value, ticker.countryCode())
                .bind(StockTickerColumns.AVAILABLE_STOCK.value, ticker.availableStock())
                .fetch()
                .rowsUpdated()
                .handle((val, sink) -> {
                    if (val == 0L) {
                        sink.error(new TransientDataAccessResourceException("could not upsert stock."));
                    }
                })
                .then(Mono.just(ticker));
    }

    private Mono<OrderReservationCandidate> fetchOrderCandidates(final ReserveItem item) {
        return template.selectOne(Query.query(Criteria
                                .where(StockTickerColumns.PRODUCT_ID.value).like(item.getProductId())
                                .and(StockTickerColumns.COUNTRY_CODE.value).like(item.getCountryCode())),
                        StockTicker.class)
                .defaultIfEmpty(new StockTicker(item.getProductId(), item.getCountryCode(), Long.MIN_VALUE))
                .flatMap(ticker -> Mono.just(ticker.withAvailableStock(ticker.availableStock() - item.getQuantity())))
                .flatMap(ticker -> Mono.just(new OrderReservationCandidate(ticker, item)));
    }
}
