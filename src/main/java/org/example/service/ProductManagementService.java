package org.example.service;

import org.example.product.Product;
import org.example.product.ProductType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 *   Manages existing products state,
 *   Normally would use a persistence layer or any kind of configuration to manage such state
 */
public class ProductManagementService {
    private List<Product> products;

    public ProductManagementService() {
        this.products = new ArrayList<>();
        initializeProducts();
    }

    private void initializeProducts() {
        products.add(new Product("Coffee", new BigDecimal("2.55"), "small", ProductType.BEVERAGE));
        products.add(new Product("Coffee", new BigDecimal("3.05"), "medium", ProductType.BEVERAGE));
        products.add(new Product("Coffee", new BigDecimal("3.55"), "large", ProductType.BEVERAGE));
        products.add(new Product("Bacon Roll", new BigDecimal("4.53"), null, ProductType.SNACK));
        products.add(new Product("Orange Juice", new BigDecimal("3.95"), null, ProductType.BEVERAGE));
        products.add(new Product("Extra milk", new BigDecimal("0.32"), null, ProductType.EXTRA));
        products.add(new Product("Foamed milk", new BigDecimal("0.51"), null, ProductType.EXTRA));
        products.add(new Product("Special roast coffee", new BigDecimal("0.95"), null, ProductType.EXTRA));
    }

    public void addNewProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public Optional<Product> findProductByNameAndSize(String name, String size) {
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name) &&
                        (p.getSize() == null || p.getSize().equals(size)))
                .findFirst();
    }

    public List<Product> findExtraProducts() {
        return products.stream()
                .filter(product -> product.getType() == ProductType.EXTRA)
                .collect(Collectors.toList());
    }
}
