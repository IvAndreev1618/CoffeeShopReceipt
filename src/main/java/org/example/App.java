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

import java.util.Arrays;
import java.util.List;

public class App {
    static ProductManagementService productManagementService = new ProductManagementServiceImpl();
    static OrderManagementService orderManagementService = new OrderManagementServiceImpl(productManagementService);
    static ReceiptService receiptService = new ReceiptServiceImpl();

    public static void main(String[] args) {

        List<InputItem> orderItems = Arrays.asList(
                new InputItem("Coffee", "small", Arrays.asList("Extra milk")),
                new InputItem("Coffee", "medium", Arrays.asList())
        );

        Input input = new Input(orderItems, 2);

        Order order = orderManagementService.processInput(input);
        String receipt = receiptService.generateReceipt(order);

        System.out.println(receipt);
    }
}
