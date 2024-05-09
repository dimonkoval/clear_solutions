package org.example.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class Sorting {
    private Sorting() {
    }

    public static Sort getSortFromRequestParam(String sortBy) {
        List<Order> orders = new ArrayList<>();
        if (sortBy.contains(":") || sortBy.contains(";")) {
            String[] sortingFields = sortBy.split(";");
            for (String field : sortingFields) {
                Order order;
                if (field.contains(":")) {
                    String[] fieldsAndDirections = field.split(":");
                    order = new Order(Direction.valueOf(fieldsAndDirections[1]),
                            fieldsAndDirections[0]);
                } else {
                    order = new Order(Direction.DESC, field);
                }
                orders.add(order);
            }
        } else {
            Order order = new Order(Direction.DESC, sortBy);
            orders.add(order);
        }
        return Sort.by(orders);
    }
}
