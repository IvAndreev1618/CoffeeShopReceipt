package org.example.order;

import java.math.BigDecimal;
import java.util.List;

public class OrderItem {

    private OrderProduct orderProduct;
    private List<OrderProduct> extraOrderProducts;

    public OrderItem(OrderProduct orderProduct, List<OrderProduct> extraOrderProducts) {
        this.orderProduct = orderProduct;
        this.extraOrderProducts = extraOrderProducts;
    }

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }

    public List<OrderProduct> getExtraOrderProducts() {
        return extraOrderProducts;
    }

    public BigDecimal calculateTotal() {
        BigDecimal extrasTotal = extraOrderProducts.stream()
                .map(OrderProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return orderProduct.getPrice().add(extrasTotal);
    }
}
