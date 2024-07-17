package org.example;

import org.example.input.Input;
import org.example.input.InputItem;
import org.example.order.Order;
import org.example.service.OrderManagementService;
import org.example.service.ProductManagementService;
import org.example.service.ReceiptService;

import java.util.Arrays;
import java.util.List;

public class App {
    private static final ProductManagementService productManagementService = new ProductManagementService();
    private static final OrderManagementService orderManagementService = new OrderManagementService(productManagementService);
    private static final ReceiptService receiptService = new ReceiptService();

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
