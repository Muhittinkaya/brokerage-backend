package com.kaya.brokerage_backend.brokerage_backend.repository;

import com.kaya.brokerage_backend.brokerage_backend.entity.Order;
import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId " +
            "AND o.createDate BETWEEN :startDate AND :endDate")
    List<Order> findByCustomerAndDateRange(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus status);

}
