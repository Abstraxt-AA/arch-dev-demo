package com.example.demo.catalog.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("product_attribute")
public record ProductAttribute(
        @Id Long attributeId,
        String attributeLabel,
        String attributeType // Can be int, bigint, decimal, varchar, blob, datetime
) {}