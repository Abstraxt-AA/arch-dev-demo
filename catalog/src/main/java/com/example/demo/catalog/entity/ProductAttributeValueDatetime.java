package com.example.demo.catalog.entity;

import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Table("product_attribute_value_datetime")
public record ProductAttributeValueDatetime(
        Long productId,
        Long attributeId,
        LocalDateTime attributeValue
) {}