package org.example;

import org.example.order.Order;
import org.example.order.OrderItem;
import org.example.order.OrderProduct;
import org.example.product.ProductType;
import org.example.service.ReceiptService;
import org.example.service.implementation.ReceiptServiceImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptServiceImplTest {

    private final ReceiptService receiptService = new ReceiptServiceImpl();

    @Test
    void testGenerateReceipt() {
        // Given
        OrderProduct product1 = new OrderProduct("Coffee", new BigDecimal("2.55"), "small", ProductType.BEVERAGE);
        OrderProduct product2 = new OrderProduct("Bacon Roll", new BigDecimal("4.53"), null, ProductType.SNACK);
        OrderProduct extraProduct = new OrderProduct("Extra milk", new BigDecimal("0.32"), null, ProductType.EXTRA);
        OrderItem orderItem1 = new OrderItem(product1, List.of(extraProduct));
        OrderItem orderItem2 = new OrderItem(product2, List.of());

        Order order = new Order(List.of(orderItem1, orderItem2), 3);

        // When
        String receipt = receiptService.generateReceipt(order);

        // Then
        String expectedReceipt =
                "Receipt:\n" +
                        "----------------------------\n" +
                        "Coffee (small): $2.55\n" +
                        " --Extra milk: $0.32\n" +
                        "Bacon Roll: $4.53\n" +
                        "----------------------------\n" +
                        "Total: $7.40" +
                        "----------------------------\n" +
                        "Current client stamps amount: 3\n";

        assertEquals(expectedReceipt, receipt);
    }

    @Test
    void testGenerateReceiptWithNoExtras() {
        // Given
        OrderProduct product1 = new OrderProduct("Coffee", new BigDecimal("2.55"), "small", ProductType.BEVERAGE);
        OrderProduct product2 = new OrderProduct("Bacon Roll", new BigDecimal("4.53"), null, ProductType.SNACK);
        OrderItem orderItem1 = new OrderItem(product1, List.of());
        OrderItem orderItem2 = new OrderItem(product2, List.of());

        Order order = new Order(List.of(orderItem1, orderItem2), 2);

        // When
        String receipt = receiptService.generateReceipt(order);

        // Then
        String expectedReceipt =
                "Receipt:\n" +
                        "----------------------------\n" +
                        "Coffee (small): $2.55\n" +
                        "Bacon Roll: $4.53\n" +
                        "----------------------------\n" +
                        "Total: $7.08" +
                        "----------------------------\n" +
                        "Current client stamps amount: 2\n";

        assertEquals(expectedReceipt, receipt);
    }

    @Test
    void testGenerateReceiptWithOnlyExtras() {
        // Given
        OrderProduct extraProduct1 = new OrderProduct("Extra milk", new BigDecimal("0.32"), null, ProductType.EXTRA);
        OrderProduct extraProduct2 = new OrderProduct("Foamed milk", new BigDecimal("0.51"), null, ProductType.EXTRA);
        OrderItem orderItem = new OrderItem(extraProduct1, List.of(extraProduct2));

        Order order = new Order(List.of(orderItem), 1);

        // When
        String receipt = receiptService.generateReceipt(order);

        // Then
        String expectedReceipt =
                "Receipt:\n" +
                        "----------------------------\n" +
                        "Extra milk: $0.32\n" +
                        " --Foamed milk: $0.51\n" +
                        "----------------------------\n" +
                        "Total: $0.83" +
                        "----------------------------\n" +
                        "Current client stamps amount: 1\n";

        assertEquals(expectedReceipt, receipt);
    }
}
