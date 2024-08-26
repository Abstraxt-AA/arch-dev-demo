package com.example.demo.catalog.entity;

import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


@Table("product_attribute_value_decimal")
public record ProductAttributeValueDecimal(
        Long productId,
        Long attributeId,
        BigDecimal attributeValue
) {}