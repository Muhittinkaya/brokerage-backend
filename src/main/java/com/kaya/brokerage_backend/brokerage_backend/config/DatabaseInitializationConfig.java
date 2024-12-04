package com.kaya.brokerage_backend.brokerage_backend.config;

import com.kaya.brokerage_backend.brokerage_backend.entity.Asset;
import com.kaya.brokerage_backend.brokerage_backend.entity.Customer;
import com.kaya.brokerage_backend.brokerage_backend.entity.Order;
import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderSide;
import com.kaya.brokerage_backend.brokerage_backend.enumaration.OrderStatus;
import com.kaya.brokerage_backend.brokerage_backend.repository.AssetRepository;
import com.kaya.brokerage_backend.brokerage_backend.repository.CustomerRepository;
import com.kaya.brokerage_backend.brokerage_backend.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DatabaseInitializationConfig {

    @Bean
    @Transactional
    public CommandLineRunner initializeDatabase(
            CustomerRepository customerRepository,
            AssetRepository assetRepository,
            OrderRepository orderRepository
    ) {
        return args -> {

            if (customerRepository.count() == 0) {
                Customer john = createCustomer("John Doe", "john@example.com", 10000.00);
                Customer jane = createCustomer("Jane Smith", "jane@example.com", 15000.50);
                Customer mike = createCustomer("Mike Johnson", "mike@example.com", 20000.00);

                customerRepository.saveAll(Arrays.asList(john, jane, mike));

                Asset johnApple = createAsset(john, "AAPL", 50.0, 50.0);
                Asset johnMicrosoft = createAsset(john, "MSFT", 30.0, 30.0);
                Asset janeGoogle = createAsset(jane, "GOOGL", 20.0, 20.0);
                Asset mikeTesla = createAsset(mike, "TSLA", 40.0, 40.0);

                assetRepository.saveAll(Arrays.asList(johnApple, johnMicrosoft, janeGoogle, mikeTesla));

                Order johnBuyOrder = createOrder(john, "AAPL", OrderSide.BUY, 10.0, 150.50);
                Order janeSellOrder = createOrder(jane, "GOOGL", OrderSide.SELL, 5.0, 120.75);
                Order mikeBuyOrder = createOrder(mike, "TSLA", OrderSide.BUY, 15.0, 200.25);

                orderRepository.saveAll(Arrays.asList(johnBuyOrder, janeSellOrder, mikeBuyOrder));
            }
        };
    }

    private Customer createCustomer(String name, String email, double balance) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setBalanceTRY(BigDecimal.valueOf(balance));
        return customer;
    }

    private Asset createAsset(Customer customer, String assetName, double size, double usableSize) {
        Asset asset = new Asset();
        asset.setCustomerId(customer.getId());
        asset.setAssetName(assetName);
        asset.setSize(BigDecimal.valueOf(size));
        asset.setUsableSize(BigDecimal.valueOf(usableSize));
        return asset;
    }

    private Order createOrder(Customer customer, String assetName, OrderSide side,
                              double size, double price) {
        Order order = new Order();
        order.setCustomerId(customer.getId());
        order.setAssetName(assetName);
        order.setOrderSide(side);
        order.setSize(BigDecimal.valueOf(size));
        order.setPrice(BigDecimal.valueOf(price));
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());
        return order;
    }
}