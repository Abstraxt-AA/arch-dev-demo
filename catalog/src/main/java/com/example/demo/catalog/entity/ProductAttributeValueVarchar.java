package com.example.demo.catalog.entity;

import org.springframework.data.relational.core.mapping.Table;


@Table("product_attribute_value_varchar")
public record ProductAttributeValueVarchar(
        Long productId,
        Long attributeId,
        String attributeValue
) {}