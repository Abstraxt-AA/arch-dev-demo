package com.example.demo.checkout.service.contract;

import com.example.demo.checkout.proto.RequestWrapper;
import com.example.demo.checkout.proto.ResponseWrapper;
import reactor.core.publisher.Mono;

public interface CheckoutService {
    Mono<ResponseWrapper> validateOrder(Mono<RequestWrapper> request);
}
