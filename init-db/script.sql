-- Create the databases
CREATE DATABASE IF NOT EXISTS catalog;
CREATE DATABASE IF NOT EXISTS checkout;
CREATE DATABASE IF NOT EXISTS stock;

-- Use catalog database
USE catalog;

-- Create product table
CREATE TABLE IF NOT EXISTS product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    is_enabled BOOLEAN NOT NULL DEFAULT 0
);

-- Create category table
CREATE TABLE IF NOT EXISTS category (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create brand table
CREATE TABLE IF NOT EXISTS brand (
    brand_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create product_attribute table
CREATE TABLE IF NOT EXISTS product_attribute (
    attribute_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attribute_label VARCHAR(50) NOT NULL UNIQUE,
    attribute_type ENUM('INT', 'BIGINT', 'DECIMAL', 'DATETIME', 'VARCHAR', 'BLOB') NOT NULL
);

-- Generate product_attribute_value_<type> tables for each type
CREATE TABLE IF NOT EXISTS product_attribute_value_int (
    product_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    attribute_value INT,
    PRIMARY KEY (product_id, attribute_id)
);

CREATE TABLE IF NOT EXISTS product_attribute_value_bigint (
    product_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    attribute_value BIGINT,
    PRIMARY KEY (product_id, attribute_id)
);

CREATE TABLE IF NOT EXISTS product_attribute_value_decimal (
    product_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    attribute_value DECIMAL(15, 3),
    PRIMARY KEY (product_id, attribute_id)
);

CREATE TABLE IF NOT EXISTS product_attribute_value_datetime (
    product_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    attribute_value DATETIME,
    PRIMARY KEY (product_id, attribute_id)
);

CREATE TABLE IF NOT EXISTS product_attribute_value_varchar (
    product_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    attribute_value VARCHAR(50),
    PRIMARY KEY (product_id, attribute_id)
);

CREATE TABLE IF NOT EXISTS product_attribute_value_blob (
    product_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    attribute_value BLOB,
    PRIMARY KEY (product_id, attribute_id)
);

-- Upsert random attributes into the product_attribute table
REPLACE INTO product_attribute (attribute_label, attribute_type) VALUES
('Weight', 'DECIMAL'),
('Color', 'VARCHAR'),
('Manufacture Date', 'DATETIME'),
('Warranty Period', 'INT'),
('Product Image', 'BLOB');

-- Use stock database
USE stock;

-- Create ticker table
CREATE TABLE IF NOT EXISTS ticker (
    product_id BIGINT NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    available_stock BIGINT NOT NULL CHECK (available_stock >= 0),
    PRIMARY KEY (product_id, country_code)
);

-- Use checkout database
USE checkout;

-- Create order table
CREATE TABLE IF NOT EXISTS order_log (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_datetime DATETIME NOT NULL
);

-- Create order_item table
CREATE TABLE IF NOT EXISTS order_item (
    product_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity >= 0),
    PRIMARY KEY (product_id, order_id),
    FOREIGN KEY (order_id) REFERENCES order_log(order_id)
);
