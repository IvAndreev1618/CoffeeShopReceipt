package org.example;

import org.example.product.Product;
import org.example.product.ProductType;
import org.example.service.ProductManagementService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductManagementServiceTest {

    private final ProductManagementService productManagementService = new ProductManagementService();

    @Test
    void testGetProducts() {
        assertEquals(8, productManagementService.getProducts().size());
    }

    @Test
    void testFindExtraProducts() {
        assertEquals(3, productManagementService.findExtraProducts().size());
    }

    @Test
    void testFindProductByCorrectNameAndSize() {
        //Given
        String name = "Coffee";
        String size = "small";

        //When
        Optional<Product> product = productManagementService.findProductByNameAndSize(name, size);

        //Then
        assertAll(
                () -> assertTrue(product.isPresent()),
                () -> assertEquals(name, product.get().getName()),
                () -> assertEquals(size, product.get().getSize()),
                () -> assertEquals(new BigDecimal("2.55"), product.get().getPrice())
        );
    }

    @Test
    void testFindProductByCorrectNameAndIncorrectSize() {
        //Given
        String name = "Coffee";
        String size = "extra-large";

        //When
        Optional<Product> product = productManagementService.findProductByNameAndSize(name, size);

        //Then
        assertFalse(product.isPresent());
    }

    @Test
    void testAddingNewProduct() {
        //Given
        Product newProduct = new Product("Latte", new BigDecimal("3.75"), "medium", ProductType.BEVERAGE);

        //When
        productManagementService.addNewProduct(newProduct);

        //Then
        Optional<Product> foundProduct = productManagementService.findProductByNameAndSize("Latte", "medium");
        assertAll(
                () -> assertTrue(foundProduct.isPresent()),
                () -> assertEquals("Latte", foundProduct.get().getName()),
                () -> assertEquals("medium", foundProduct.get().getSize()),
                () -> assertEquals(new BigDecimal("3.75"), foundProduct.get().getPrice())
        );
    }
}
