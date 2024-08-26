package com.example.demo.checkout.controller;

import com.example.demo.checkout.proto.RequestWrapper;
import com.example.demo.checkout.proto.ResponseWrapper;
import com.example.demo.checkout.service.contract.CheckoutService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(final CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PutMapping({"/validate", "/v1/validate"})
    public Mono<ResponseWrapper> validateOrder(final @RequestBody Mono<RequestWrapper> request) {
        return checkoutService.validateOrder(request);
    }
}
