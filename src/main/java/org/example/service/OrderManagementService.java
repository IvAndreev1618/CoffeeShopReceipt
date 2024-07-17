package org.example.service;

import org.example.input.Input;
import org.example.order.Order;

public interface OrderManagementService {
    Order processInput(Input input);
}
