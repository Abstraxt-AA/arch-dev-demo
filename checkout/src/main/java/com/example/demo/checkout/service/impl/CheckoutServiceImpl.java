package com.example.demo.checkout.service.impl;

import com.example.demo.checkout.pojo.StockAndStatus;
import com.example.demo.checkout.proto.RequestWrapper;
import com.example.demo.checkout.proto.ResponseWrapper;
import com.example.demo.checkout.proto.ValidateOrderResponse;
import com.example.demo.checkout.service.contract.CheckoutService;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final ReactiveRedisOperations<String, StockAndStatus> redisOps;

    public CheckoutServiceImpl(final ReactiveRedisOperations<String, StockAndStatus> redisOps) {
        this.redisOps = redisOps;
    }

    @Override
    public Mono<ResponseWrapper> validateOrder(final Mono<RequestWrapper> request) {
        return request.flatMap(wrapper -> Mono.just(wrapper.getValidateOrderRequest()))
                .flatMapMany(order -> Flux.fromStream(order.getItemsList().stream())
                        .flatMap(item -> {
                            final var key = StockAndStatus.CHECKOUT_REDIS_KEY + ":" +
                                    item.getProductId() + ":" + order.getCountryCode().toUpperCase();
                            return redisOps.opsForValue()
                                    .get(key)
                                    .switchIfEmpty(Mono.just(new StockAndStatus(false, 0L)))
                                    .flatMap(status -> Mono.just(status.withStock(status.stock() -
                                            item.getQuantity())));
                        }))
                .any(value -> !value.status() || value.stock() < 0)
                .flatMap(check -> {
                    if (Boolean.TRUE.equals(check)) {
                        return Mono.just(ResponseWrapper.newBuilder()
                                .setValidateOrderResponse(ValidateOrderResponse.newBuilder()
                                        .setIsValid(false)).build());
                    }
                    return Mono.just(ResponseWrapper.newBuilder()
                            .setValidateOrderResponse(ValidateOrderResponse.newBuilder()
                                    .setIsValid(true)).build());
                });
    }
}
