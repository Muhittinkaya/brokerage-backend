package com.kaya.brokerage_backend.brokerage_backend.controller;

import com.kaya.brokerage_backend.brokerage_backend.entity.Order;
import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderSide;
import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderStatus;
import com.kaya.brokerage_backend.brokerage_backend.repository.OrderRepository;
import com.kaya.brokerage_backend.brokerage_backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/create")
    public Order createOrder(
            @RequestParam Long customerId,
            @RequestParam String assetName,
            @RequestParam OrderSide side,
            @RequestParam BigDecimal size,
            @RequestParam BigDecimal price
    ) throws Exception {
        return orderService.createOrder(customerId, assetName, side, size, price);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long customerId
    ) throws Exception {
        orderService.cancelOrder(orderId, customerId);
    }

    @GetMapping("/list")
    public List<Order> listOrders(
            @RequestParam Long customerId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(required = false) OrderStatus status
    ) {
        List<Order> orders = orderRepository.findByCustomerAndDateRange(
                customerId, startDate, endDate
        );

        if (status != null) {
            orders.removeIf(order -> order.getOrderStatus() != status);
        }

        return orders;
    }
}
