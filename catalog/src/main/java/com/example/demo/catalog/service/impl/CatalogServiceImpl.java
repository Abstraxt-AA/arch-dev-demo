package com.example.demo.catalog.service.impl;

import com.example.demo.catalog.entity.Product;
import com.example.demo.catalog.proto.RequestWrapper;
import com.example.demo.catalog.service.contract.CatalogService;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final R2dbcEntityTemplate template;

    public CatalogServiceImpl(final R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    @Transactional
    public Mono<Product> createProduct(final Mono<RequestWrapper> request) {
        return request
                .flatMap(val -> Mono.just(val.getCreateProductRequest()))
                .flatMap(val -> Mono.just(new Product(null, val.getSku(), val.getIsEnabled())))
                .flatMap(template::insert);
    }
}
