package org.example.order;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private List<OrderItem> orderItems;

    private Integer currentStampsAmount;

    public Order(List<OrderItem> orderItems, Integer currentStampsAmount) {
        this.orderItems = orderItems;
        this.currentStampsAmount = currentStampsAmount;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Integer getCurrentStampsAmount() {
        return currentStampsAmount;
    }

    public BigDecimal calculateTotal() {
        return orderItems.stream()
                .map(OrderItem::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
