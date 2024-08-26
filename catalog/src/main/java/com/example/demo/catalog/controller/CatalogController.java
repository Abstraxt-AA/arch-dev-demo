package com.example.demo.catalog.controller;

import com.example.demo.catalog.proto.RequestWrapper;
import com.example.demo.catalog.proto.ResponseWrapper;
import com.example.demo.catalog.service.contract.CatalogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(final CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * Method to create a product. Does not include creating product attributes, as the product attributes are there
     * to demo how 5NF, 6NF, or EAV schemas can be modeled in code.
     * @param request create product request.
     * @return a response with the created product details.
     */
    @PostMapping({"/create", "/v1/create"})
    public Mono<ResponseWrapper> createProduct(final @RequestBody Mono<RequestWrapper> request) {
        return catalogService.createProduct(request)
                .flatMap(val -> Mono.just(ResponseWrapper.newBuilder()
                        .setCreateProductResponse(val.toCreateProductResponse())
                        .build()));
    }
}
