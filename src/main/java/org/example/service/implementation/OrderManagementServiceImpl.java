package org.example.service.implementation;

import org.example.exception.ProductValidationException;
import org.example.input.Input;
import org.example.input.InputItem;
import org.example.order.Order;
import org.example.order.OrderItem;
import org.example.order.OrderProduct;
import org.example.product.Product;
import org.example.product.ProductType;
import org.example.service.OrderManagementService;
import org.example.service.ProductManagementService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages processing of input to order.
 * Applies existing bonus programs and checkups
 * Order class serves a purpose of DTO, created based on the data from Input and data stored in Product
 */
public class OrderManagementServiceImpl implements OrderManagementService {
    private final ProductManagementService productManagementService;
    private static final Integer BONUS_PROGRAM_AMOUNT = 5;

    public OrderManagementServiceImpl(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @Override
    public Order processInput(Input input) {
        validateInputOrder(input);
        List<OrderItem> products = convertInputToOrder(input);
        Integer stamps = applyStampsBonusProgram(products, input.getStampsAmount());
        applyComboBonusProgram(products);
        return new Order(products, stamps);
    }

    private void validateInputOrder(Input input) {
        List<InputItem> nonValidProducts = input.getInputItems().stream()
                .filter(inputItem -> !isValidProduct(inputItem))
                .peek(inputItem -> System.out.println("System can not proceed with this product: " +
                        inputItem.getProductName() +
                        ", it will be excluded from the receipt"))
                .collect(Collectors.toList());

        input.getInputItems().removeIf(nonValidProducts::contains);

        validateExtraProducts(input);
    }

    private void validateExtraProducts(Input input) {
        input.getInputItems().forEach(inputItem -> {
            if (inputItem.getExtraProducts() != null) {
                List<String> nonValidExtras = inputItem.getExtraProducts().stream()
                        .filter(extra -> !isValidExtraProduct(extra))
                        .peek(extra -> System.out.println("Extra product: " + extra +
                                " for the product: " + inputItem.getProductName() +
                                ", is not eligible and will be excluded from the receipt"))
                        .collect(Collectors.toList());
                inputItem.getExtraProducts().removeAll(nonValidExtras);
            }
        });
    }

    private boolean isValidProduct(InputItem inputItem) {
        return productManagementService.findProductByNameAndSize(inputItem.getProductName(), inputItem.getSize()).isPresent();
    }

    private boolean isValidExtraProduct(String extraProductName) {
        return productManagementService.findExtraProducts().stream()
                .anyMatch(product -> product.getName().equalsIgnoreCase(extraProductName));
    }

    /*
     *  Converts input to DTO(Order classes) based on the data from Product
     */
    private List<OrderItem> convertInputToOrder(Input input) {
        return input.getInputItems().stream()
                .map(this::convertInputItemToOrderItem)
                .collect(Collectors.toList());
    }

    /*
     *  Converts inputItem to orderItem
     */
    private OrderItem convertInputItemToOrderItem(InputItem item) {
        OrderProduct orderProduct = productToOrderProduct(findProduct(item.getProductName(), item.getSize()));
        List<OrderProduct> extras = item.getExtraProducts() != null
                ? item.getExtraProducts().stream()
                .map(extraProductName -> productToOrderProduct(findProduct(extraProductName, null)))
                .collect(Collectors.toList())
                : new ArrayList<>();
        return new OrderItem(orderProduct, extras);
    }

    private OrderProduct productToOrderProduct(Product product) {
        return new OrderProduct(product.getName(), product.getPrice(), product.getSize(), product.getType());
    }

    private Product findProduct(String productName, String size) {
        return productManagementService.findProductByNameAndSize(productName, size)
                .orElseThrow(() -> new ProductValidationException("Product not found: " + productName + (size != null ? " " + size : "")));
    }

    /*
     *  Applies stamps bonus program to beverages from order
     *  Checks current stamps amount from input, applies corresponding amount of discount to beverages in order
     *  Returns new stamps amount
     * */
    private Integer applyStampsBonusProgram(List<OrderItem> orderItems, Integer stampsAmount) {
        List<OrderProduct> beveragesProducts = getProductsByType(orderItems, ProductType.BEVERAGE);
        int beveragesCount = beveragesProducts.size();
        int beveragesAndStamps = beveragesCount + stampsAmount;
        int bonusApplications = beveragesAndStamps / BONUS_PROGRAM_AMOUNT;
        int newStampsAmount = beveragesAndStamps % BONUS_PROGRAM_AMOUNT;

        applyDiscountToProducts(beveragesProducts, bonusApplications);
        return newStampsAmount;
    }

    /*
     *  Applies combo bonus program to items from order
     *  Checks how many pairs of beverage and snack exists and applies it to the extras
     * */
    private void applyComboBonusProgram(List<OrderItem> orderItems) {
        int beveragesCount = getProductsByType(orderItems, ProductType.BEVERAGE).size();
        int snacksCount = getProductsByType(orderItems, ProductType.SNACK).size();
        int beverageSnackPairCount = Math.min(beveragesCount, snacksCount);

        List<OrderProduct> extraProducts = new ArrayList<>();
        orderItems.forEach(orderItem -> extraProducts.addAll(orderItem.getExtraOrderProducts()));
        applyDiscountToProducts(extraProducts, beverageSnackPairCount);
    }

    private void applyDiscountToProducts(List<OrderProduct> products, int discountCount) {
        for (int i = 0; i < discountCount && i < products.size(); i++) {
            products.stream()
                    .filter(product -> product.getPrice().compareTo(BigDecimal.ZERO) > 0)
                    .findFirst()
                    .ifPresent(product -> product.setPrice(BigDecimal.ZERO));
        }
    }

    private List<OrderProduct> getProductsByType(List<OrderItem> orderItems, ProductType productType) {
        return orderItems.stream()
                .map(OrderItem::getOrderProduct)
                .filter(product -> product.getProductType() == productType)
                .collect(Collectors.toList());
    }
}