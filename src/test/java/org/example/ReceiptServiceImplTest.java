package org.example;

import org.example.input.Input;
import org.example.input.InputItem;
import org.example.order.Order;
import org.example.service.OrderManagementService;
import org.example.service.ProductManagementService;
import org.example.service.ReceiptService;
import org.example.service.implementation.OrderManagementServiceImpl;
import org.example.service.implementation.ProductManagementServiceImpl;
import org.example.service.implementation.ReceiptServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptServiceImplTest {

    private final ReceiptService receiptService = new ReceiptServiceImpl();
    private final ProductManagementService productManagementService = new ProductManagementServiceImpl();
    private final OrderManagementService orderManagementService = new OrderManagementServiceImpl(productManagementService);

    @Test
    void testGenerateReceipt() {
        // Given
        Input input = new Input(List.of(new InputItem("Coffee", "small", List.of("Extra milk")),
                new InputItem("Bacon Roll", null, List.of())
                ), 3);

        Order order = orderManagementService.processInput(input);

        // When
        String receipt = receiptService.generateReceipt(order);

        // Then
        String expectedReceipt =
                "Receipt:\n" +
                        "----------------------------\n" +
                        "Coffee (small): $2.55\n" +
                        " --Extra milk: $0\n" +
                        "Bacon Roll: $4.53\n" +
                        "----------------------------\n" +
                        "Total: $7.08" +
                        "----------------------------\n" +
                        "Current client stamps amount: 4\n";

        assertEquals(expectedReceipt, receipt);
    }

    @Test
    void testGenerateReceiptWithNoExtras() {
        // Given
        Input input = new Input(List.of(new InputItem("Coffee", "small", List.of()),
                new InputItem("Bacon Roll", null, List.of())
        ), 2);

        Order order = orderManagementService.processInput(input);

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
                        "Current client stamps amount: 3\n";

        assertEquals(expectedReceipt, receipt);
    }

}
