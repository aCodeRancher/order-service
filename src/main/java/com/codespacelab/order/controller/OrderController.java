package com.codespacelab.order.controller;

import com.codespacelab.order.model.OrderDto;
import com.codespacelab.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Value("${spring.datasource.url}")
    private String database;

    private OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public List<OrderDto> getOrders() {
        log.info("database url: " + database);

        return orderService.getOrders();
    }

    @GetMapping
    public OrderDto getOrder(
            @RequestParam(name = "id") Long id
    ) {
        return orderService.getOrder(id);
    }

    @PostMapping
    public OrderDto addOrder(
            @Validated @RequestBody OrderDto orderDto
    ) {
        return orderService.addOrder(orderDto);
    }

    @PutMapping
    public OrderDto updateOrder(
            @Validated @RequestBody OrderDto orderDto
    ) {
        return orderService.updateOrder(orderDto);
    }

    @DeleteMapping
    public boolean deleteOrder(
            @RequestParam(name = "id") Long id
    ) {
        return orderService.deleteOrder(id);
    }
}
