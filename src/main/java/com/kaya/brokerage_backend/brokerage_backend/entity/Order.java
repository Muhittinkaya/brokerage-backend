package com.kaya.brokerage_backend.brokerage_backend.entity;

import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderSide;
import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long customerId;
    @Column(nullable = false)
    private String assetName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide orderSide;
    @Column(nullable = false)
    private BigDecimal size;
    @Column(nullable = false)
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private LocalDateTime createDate;
}
