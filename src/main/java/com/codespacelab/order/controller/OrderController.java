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
    public List<OrderDto> getOrders(
            @RequestParam(name = "userId") Long userId
    ) {
        log.info("database url: " + database);

        return orderService.getOrders(userId);
    }

    @GetMapping
    public OrderDto getOrder(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "userId") Long userId
    ) {
        return orderService.getOrder(id ,userId);
    }

    @PostMapping
    public OrderDto addOrder(
            @Validated @RequestBody OrderDto orderDto,
            @RequestParam(name = "userId") Long userId
    ) {
        return orderService.addOrder(orderDto, userId);
    }

    @PutMapping
    public OrderDto updateOrder(
            @Validated @RequestBody OrderDto orderDto,
            @RequestParam(name = "userId") Long userId
    ) {
        return orderService.updateOrder(orderDto, userId);
    }

    @DeleteMapping
    public boolean deleteOrder(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "userId") Long userId
    ) {
        return orderService.deleteOrder(id, userId);
    }
}
