package com.kaya.brokerage_backend.brokerage_backend.service;

import com.kaya.brokerage_backend.brokerage_backend.entity.Asset;
import com.kaya.brokerage_backend.brokerage_backend.entity.Customer;
import com.kaya.brokerage_backend.brokerage_backend.entity.Order;
import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderSide;
import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderStatus;
import com.kaya.brokerage_backend.brokerage_backend.repository.AssetRepository;
import com.kaya.brokerage_backend.brokerage_backend.repository.CustomerRepository;
import com.kaya.brokerage_backend.brokerage_backend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AssetRepository assetRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, AssetRepository assetRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.assetRepository = assetRepository;
    }

    @Transactional
    public Order createOrder(Long customerId, String assetName, OrderSide side,
                             BigDecimal size, BigDecimal price) throws Exception {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new Exception("Customer not found"));

        // Validate and process order based on side (BUY/SELL)
        if (side == OrderSide.BUY) {
            validateAndProcessBuyOrder(customer, assetName, size, price);
        } else {
            validateAndProcessSellOrder(customer, assetName, size, price);
        }

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setAssetName(assetName);
        order.setOrderSide(side);
        order.setSize(size);
        order.setPrice(price);
        order.setOrderStatus(OrderStatus.MATCHED);
        order.setCreateDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, Long customerId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if (!order.getCustomerId().equals(customerId)) {
            throw new Exception("Not authorized to cancel this order");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new Exception("Only PENDING orders can be canceled");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new Exception("Customer not found"));

        if (order.getOrderSide() == OrderSide.BUY) {

            customer.setBalanceTRY(customer.getBalanceTRY().add(
                    order.getPrice().multiply(order.getSize())
            ));
        } else {

            Asset asset = assetRepository.findByCustomerIdAndAssetName(
                    customerId, order.getAssetName()
            );
            asset.setUsableSize(asset.getUsableSize().add(order.getSize()));
            customer.setBalanceTRY(customer.getBalanceTRY().subtract(
                    order.getPrice().multiply(order.getSize())
            ));
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        customerRepository.save(customer);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> listOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        if (customerId == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Customer ID, startDate, and endDate are required");
        }
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate);
    }

    private void validateAndProcessBuyOrder(Customer customer, String assetName,
                                            BigDecimal size, BigDecimal price) throws Exception {
        BigDecimal totalCost = size.multiply(price);
        if (customer.getBalanceTRY().compareTo(totalCost) < 0) {
            throw new Exception("Insufficient TRY balance");
        }

        // Deduct funds
        customer.setBalanceTRY(customer.getBalanceTRY().subtract(totalCost));
        customerRepository.save(customer);

        Asset asset = assetRepository.findByCustomerIdAndAssetName(customer.getId(), assetName);
        if (asset == null) {
            asset = new Asset();
        }
        asset.setAssetName(assetName);
        asset.setUsableSize(size);
        asset.setCustomerId(customer.getId());
        asset.setSize(size);
        assetRepository.save(asset);
    }

    private void validateAndProcessSellOrder(Customer customer, String assetName,
                                             BigDecimal size, BigDecimal price) throws Exception {
        Asset asset = assetRepository.findByCustomerIdAndAssetName(customer.getId(), assetName);
        if (asset == null || asset.getUsableSize().compareTo(size) < 0) {
            throw new Exception("Insufficient asset size");
        }

        BigDecimal totalCostAfterSell = size.multiply(price);

        // Add to funds
        customer.setBalanceTRY(customer.getBalanceTRY().add(totalCostAfterSell));
        customerRepository.save(customer);

        // Reserve assets for selling
        asset.setUsableSize(asset.getUsableSize().subtract(size));
        assetRepository.save(asset);
    }
}
