package org.example.service;

import org.example.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductManagementService {
    void addNewProduct(Product product);

    List<Product> getProducts();

    Optional<Product> findProductByNameAndSize(String name, String size);

    List<Product> findExtraProducts();
}
