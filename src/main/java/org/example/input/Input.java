package org.example.input;

import java.util.List;

public class Input {
    private final List<InputItem> inputItems;
    private final Integer stampsAmount;

    public Input(List<InputItem> orderItems, Integer stampsAmount) {
        this.inputItems = orderItems;
        this.stampsAmount = stampsAmount;
    }

    public List<InputItem> getInputItems() {
        return inputItems;
    }

    public Integer getStampsAmount() {
        return stampsAmount;
    }
}
