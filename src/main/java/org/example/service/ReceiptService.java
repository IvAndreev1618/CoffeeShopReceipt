package org.example.service;

import org.example.order.Order;

public interface ReceiptService {

    String generateReceipt(Order order);
}
