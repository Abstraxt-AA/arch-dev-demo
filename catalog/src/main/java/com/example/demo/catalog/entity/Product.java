package com.example.demo.catalog.entity;

import com.example.demo.catalog.proto.CreateProductResponse;
import com.example.demo.catalog.proto.ProductDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("product")
public record Product(
        @Id Long productId,
        String sku,
        Boolean isEnabled
) {

    public CreateProductResponse toCreateProductResponse() {
        final var productDto = ProductDto.newBuilder()
                .setSku(this.sku)
                .setIsEnabled(this.isEnabled)
                .build();
        return CreateProductResponse.newBuilder()
                .setId(this.productId)
                .setProduct(productDto)
                .build();
    }
}
