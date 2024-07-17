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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    static ProductManagementService productManagementService = new ProductManagementServiceImpl();
    static OrderManagementService orderManagementService = new OrderManagementServiceImpl(productManagementService);
    static ReceiptService receiptService = new ReceiptServiceImpl();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter each product your client purchased.");
        System.out.println("Type 'stamp' with space followed by number to set the number of stamps that client has.");
        System.out.println("Type 'print' to start printing the receipt.");
        System.out.println("Type 'exit' to close the program.");

        while (true) {
            List<InputItem> inputItems = new ArrayList<>();
            int stampsAmount = 0;
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("print")) {
                    break;
                }
                if (input.toLowerCase().startsWith("stamp ")) {
                    stampsAmount = Integer.parseInt(input.split("\\s+")[1]);
                    continue;
                }
                if (input.equalsIgnoreCase("exit")) {
                    scanner.close();
                    return;
                }
                inputItems.add(convertToInputItem(input));
            }

            Input input = new Input(inputItems, stampsAmount);
            Order order = orderManagementService.processInput(input);
            String receipt = receiptService.generateReceipt(order);
            System.out.println(receipt);
        }
    }

    private static InputItem convertToInputItem(String item) {
        List<String> validSizes = List.of("small", "medium", "large");
        String[] parts = item.split("\\s+");
        List<String> inputMainProduct;
        List<String> inputExtras;
        List<String> extraProducts = null;

        String inputItemSize = (parts.length > 0 && validSizes.contains(parts[0].toLowerCase())) ? parts[0].toLowerCase() : null;
        int itemStartIndex = (inputItemSize != null) ? 1 : 0;

        List<String> inputExceptSize = Stream.of(parts).skip(itemStartIndex).collect(Collectors.toList());

        int extrasIndex = inputExceptSize.indexOf("with");

        if(extrasIndex != -1) {
            inputMainProduct = inputExceptSize.subList(0, extrasIndex);
            inputExtras = inputExceptSize.subList(extrasIndex + 1, inputExceptSize.size());
        } else {
            inputMainProduct = inputExceptSize;
            inputExtras = List.of();
        }

        String productName = inputMainProduct.stream().collect(Collectors.joining(" "));

        if(!inputExtras.isEmpty()) {
            extraProducts = Stream.of(String.join(" ", inputExtras).split("\\s*and\\s*"))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        return new InputItem(productName, inputItemSize, extraProducts);
    }
}

