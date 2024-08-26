package com.example.demo.catalog.entity;

import org.springframework.data.relational.core.mapping.Table;


@Table("product_attribute_value_int")
public record ProductAttributeValueInt(
        Long productId,
        Long attributeId,
        Integer attributeValue
) {}