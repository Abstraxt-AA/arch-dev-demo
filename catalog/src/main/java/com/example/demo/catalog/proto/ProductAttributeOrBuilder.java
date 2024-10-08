// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: src/main/resources/messages.proto
// Protobuf Java Version: 4.27.3

package com.example.demo.catalog.proto;

public interface ProductAttributeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.example.demo.catalog.ProductAttribute)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 attribute_id = 1;</code>
   * @return The attributeId.
   */
  long getAttributeId();

  /**
   * <code>int32 int_value = 2;</code>
   * @return Whether the intValue field is set.
   */
  boolean hasIntValue();
  /**
   * <code>int32 int_value = 2;</code>
   * @return The intValue.
   */
  int getIntValue();

  /**
   * <code>int64 bigint_value = 3;</code>
   * @return Whether the bigintValue field is set.
   */
  boolean hasBigintValue();
  /**
   * <code>int64 bigint_value = 3;</code>
   * @return The bigintValue.
   */
  long getBigintValue();

  /**
   * <code>double decimal_value = 4;</code>
   * @return Whether the decimalValue field is set.
   */
  boolean hasDecimalValue();
  /**
   * <code>double decimal_value = 4;</code>
   * @return The decimalValue.
   */
  double getDecimalValue();

  /**
   * <code>string varchar_value = 5;</code>
   * @return Whether the varcharValue field is set.
   */
  boolean hasVarcharValue();
  /**
   * <code>string varchar_value = 5;</code>
   * @return The varcharValue.
   */
  java.lang.String getVarcharValue();
  /**
   * <code>string varchar_value = 5;</code>
   * @return The bytes for varcharValue.
   */
  com.google.protobuf.ByteString
      getVarcharValueBytes();

  /**
   * <code>bytes blob_value = 6;</code>
   * @return Whether the blobValue field is set.
   */
  boolean hasBlobValue();
  /**
   * <code>bytes blob_value = 6;</code>
   * @return The blobValue.
   */
  com.google.protobuf.ByteString getBlobValue();

  /**
   * <code>string datetime_value = 7;</code>
   * @return Whether the datetimeValue field is set.
   */
  boolean hasDatetimeValue();
  /**
   * <code>string datetime_value = 7;</code>
   * @return The datetimeValue.
   */
  java.lang.String getDatetimeValue();
  /**
   * <code>string datetime_value = 7;</code>
   * @return The bytes for datetimeValue.
   */
  com.google.protobuf.ByteString
      getDatetimeValueBytes();

  com.example.demo.catalog.proto.ProductAttribute.ValueCase getValueCase();
}
