package org.example;

import org.example.exception.ProductValidationException;
import org.example.input.Input;
import org.example.input.InputItem;
import org.example.order.Order;
import org.example.service.OrderManagementService;
import org.example.service.ProductManagementService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderManagementServiceTest {

    private final ProductManagementService productManagementService = new ProductManagementService();
    private final OrderManagementService orderManagementService = new OrderManagementService(productManagementService);

    @Test
    void testBasicInput() {
        //Given
        List<InputItem> inputItemList = List.of(
                new InputItem("Coffee", "small", List.of()),
                new InputItem("Coffee", "medium", List.of()),
                new InputItem("Orange Juice", null, List.of())
        );
        Input basicInput = new Input(inputItemList, 1);

        //When
        Order order = orderManagementService.processInput(basicInput);

        //Then
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(4, order.getCurrentStampsAmount()),
                () -> assertEquals(new BigDecimal("9.55"), order.calculateTotal())
        );
    }

    @Test
    void testStampBonusBasicConditions() {
        //Given
        List<InputItem> inputItemList = List.of(
                new InputItem("Coffee", "small", List.of()),
                new InputItem("Coffee", "medium", List.of()),
                new InputItem("Orange Juice", null, List.of())
        );
        Input basicInput = new Input(inputItemList, 3);

        //When
        Order order = orderManagementService.processInput(basicInput);

        //Then
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(1, order.getCurrentStampsAmount()),
                () -> assertEquals(new BigDecimal("7.00"), order.calculateTotal())
        );
    }

    @Test
    void testStampBonusMultipleProducts() {
        //Given
        List<InputItem> inputItemList = List.of(
                new InputItem("Coffee", "small", List.of()),
                new InputItem("Coffee", "medium", List.of()),
                new InputItem("Coffee", "medium", List.of()),
                new InputItem("Coffee", "medium", List.of()),
                new InputItem("Orange Juice", null, List.of()),
                new InputItem("Orange Juice", null, List.of())
        );
        Input basicInput = new Input(inputItemList, 4);

        //When
        Order order = orderManagementService.processInput(basicInput);

        //Then
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(0, order.getCurrentStampsAmount()),
                () -> assertEquals(new BigDecimal("14.00"), order.calculateTotal())
        );
    }

    @Test
    void testComboBonusConditions() {
        //Given
        List<InputItem> inputItemList = List.of(
                new InputItem("Coffee", "small", List.of("Extra milk")),
                new InputItem("Bacon Roll", null, List.of())
        );
        Input basicInput = new Input(inputItemList, 2);

        //When
        Order order = orderManagementService.processInput(basicInput);

        //Then
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(3, order.getCurrentStampsAmount()),
                () -> assertEquals(new BigDecimal("7.08"), order.calculateTotal())
        );
    }

    @Test
    void testComboBonusMultipleItems() {
        //Given
        List<InputItem> inputItemList = List.of(
                new InputItem("Coffee", "small", List.of("Extra milk", "Special roast coffee")),
                new InputItem("Bacon Roll", null, List.of()),
                new InputItem("Coffee", "small", List.of("Extra milk")),
                new InputItem("Bacon Roll", null, List.of())
        );
        Input basicInput = new Input(inputItemList, 2);

        //When
        Order order = orderManagementService.processInput(basicInput);

        //Then
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(4, order.getCurrentStampsAmount()),
                () -> assertEquals(new BigDecimal("14.48"), order.calculateTotal())
        );
    }

    @Test
    void testUnexpectedProducts() {
        //Given
        List<InputItem> inputItemList = List.of(
                new InputItem("Coffee11", "small", List.of("Extra milk", "Special roast coffee"))
        );
        Input basicInput = new Input(inputItemList, 2);

        assertThrows(ProductValidationException.class, () -> orderManagementService.processInput(basicInput));
    }

}
