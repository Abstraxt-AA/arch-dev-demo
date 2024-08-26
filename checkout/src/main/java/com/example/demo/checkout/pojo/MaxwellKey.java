package com.example.demo.checkout.pojo;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public record MaxwellKey(String database, String table,
                         Map<String, String> primaryKey) {

    public MaxwellKey {
        primaryKey = primaryKey == null
                ? new HashMap<>()
                : primaryKey;
    }

    @JsonAnySetter
    public void set(final String key, final Object value) {
        this.primaryKey.put(key, value.toString());
    }
}
