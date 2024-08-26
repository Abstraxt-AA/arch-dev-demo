package com.example.demo.catalog.entity;

import org.springframework.data.relational.core.mapping.Table;


@Table("product_attribute_value_bigint")
public record ProductAttributeValueBigInt(
        Long productId,
        Long attributeId,
        Long attributeValue
) {}