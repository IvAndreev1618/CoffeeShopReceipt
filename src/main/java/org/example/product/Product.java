package org.example.product;

import java.math.BigDecimal;

public class Product {

    private final String name;
    private final BigDecimal price;
    private final String size;
    private final ProductType productType;

    public Product(String name, BigDecimal price, String size, ProductType productType) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public ProductType getType() {
        return productType;
    }

}
