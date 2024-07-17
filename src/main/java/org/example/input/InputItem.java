package org.example.input;

import java.util.List;

public class InputItem {
    private String productName;
    private String size;
    private List<String> extraProducts;

    public InputItem(String productName, String size, List<String> extraProducts) {
        this.productName = productName;
        this.size = size;
        this.extraProducts = extraProducts;
    }

    public String getProductName() {
        return productName;
    }

    public String getSize() {
        return size;
    }

    public List<String> getExtraProducts() {
        return extraProducts;
    }
}
