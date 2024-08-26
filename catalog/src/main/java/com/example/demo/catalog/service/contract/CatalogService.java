package com.example.demo.catalog.service.contract;

import com.example.demo.catalog.entity.Product;
import com.example.demo.catalog.proto.RequestWrapper;
import reactor.core.publisher.Mono;

public interface CatalogService {

    Mono<Product> createProduct(Mono<RequestWrapper> request);
}
