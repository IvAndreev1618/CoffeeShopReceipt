package org.example.service;

import org.example.order.Order;
import org.example.order.OrderItem;
import org.example.order.OrderProduct;

public class ReceiptService {

    private static final String LIMITER = "----------------------------\n";

    public String generateReceipt(Order order) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Receipt:\n")
                .append(LIMITER);

        for (OrderItem orderItem : order.getOrderItems()) {
            appendOrderItem(receipt, orderItem);
        }

        receipt.append(LIMITER)
                .append("Total: $")
                .append(order.calculateTotal())
                .append(LIMITER)
                .append("Current client stamps amount: ")
                .append(order.getCurrentStampsAmount())
                .append("\n");

        return receipt.toString();
    }

    private void appendOrderItem(StringBuilder receipt, OrderItem orderItem) {
        OrderProduct mainProduct = orderItem.getOrderProduct();
        receipt.append(mainProduct.getName());
        if (mainProduct.getSize() != null) {
            receipt.append(" (").append(mainProduct.getSize()).append(")");
        }
        receipt.append(": $").append(mainProduct.getPrice()).append("\n");

        for (OrderProduct extraProduct : orderItem.getExtraOrderProducts()) {
            receipt.append(" --").append(extraProduct.getName())
                    .append(": $").append(extraProduct.getPrice()).append("\n");
        }
    }
}