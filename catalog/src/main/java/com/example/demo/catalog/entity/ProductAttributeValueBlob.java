package com.example.demo.catalog.entity;

import org.springframework.data.relational.core.mapping.Table;


@Table("product_attribute_value_blob")
public record ProductAttributeValueBlob(
        Long productId,
        Long attributeId,
        byte[] attributeValue
) {}