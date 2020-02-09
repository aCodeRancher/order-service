package com.codespacelab.order.service;

import com.codespacelab.order.model.OrderDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private List<OrderDto> orders;

    public OrderService() {
        OrderDto order1 = new OrderDto(123L, Arrays.asList("Burger", "Meal"), "Collected");
        OrderDto order2 = new OrderDto(456L, Arrays.asList("Burger", "Meal", "Meal"), "Pick-up");

        orders = new ArrayList<>(Arrays.asList(order1, order2));
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public OrderDto getOrder(Long id) {
        Optional<OrderDto> orderOptional = orders.stream().filter(order -> order.getId().equals(id)).findFirst();
        return orderOptional.isPresent() ? orderOptional.get() : null;
    }

    public OrderDto addOrder(OrderDto orderDto) {
        OrderDto newOrder = new OrderDto(orders.size() + 100L, orderDto.getItems(), "Pending");
        orders.add(newOrder);
        return newOrder;
    }

    public OrderDto updateOrder(OrderDto orderDto) {
        Optional<OrderDto> orderOptional = orders.stream()
                .filter(order -> order.getId() == orderDto.getId())
                .map(order -> {
                    order.setItems(orderDto.getItems());
                    order.setStatus(orderDto.getStatus());
                    return order;
                }).findFirst();

        return orderOptional.isPresent() ? orderOptional.get() : null;
    }

    public boolean deleteOrder(Long id) {
        final int originalSize = orders.size();
        orders = orders.stream().filter(order -> !order.getId().equals(id)).collect(Collectors.toList());

        return originalSize > orders.size();
    }
}
