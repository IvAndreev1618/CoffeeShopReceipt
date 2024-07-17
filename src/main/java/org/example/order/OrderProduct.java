package org.example.order;

import org.example.product.ProductType;

import java.math.BigDecimal;

public class OrderProduct {

    private final String name;
    private BigDecimal price;
    private final String size;
    private final ProductType productType;

    public OrderProduct(String name, BigDecimal price, String size, ProductType productType) {
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public ProductType getProductType() {
        return productType;
    }
}
