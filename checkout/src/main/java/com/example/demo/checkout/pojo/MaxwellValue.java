package com.example.demo.checkout.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;

public record MaxwellValue(String database, RecordType type, String table, @JsonProperty("ts") Long timestamp,
                           @JsonProperty("xid") Long transactionId,
                           Boolean commit, Map<String, String> data) {

    enum RecordType {
        INSERT("insert"),
        UPDATE("update"),
        DELETE("delete");

        private final String type;

        RecordType(final String type) {
            this.type = type;
        }

        @JsonCreator
        public static RecordType forType(final String type) {
            for (final var recordType : RecordType.values()) {
                if (recordType.type.equals(type)) {
                    return recordType;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return this.type;
        }
    }
}
