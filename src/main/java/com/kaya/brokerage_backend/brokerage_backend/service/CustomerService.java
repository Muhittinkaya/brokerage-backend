package com.kaya.brokerage_backend.brokerage_backend.service;

import com.kaya.brokerage_backend.brokerage_backend.entity.Asset;
import com.kaya.brokerage_backend.brokerage_backend.entity.Customer;
import com.kaya.brokerage_backend.brokerage_backend.repository.AssetRepository;
import com.kaya.brokerage_backend.brokerage_backend.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AssetRepository assetRepository;

    public CustomerService(CustomerRepository customerRepository, AssetRepository assetRepository) {
        this.customerRepository = customerRepository;
        this.assetRepository = assetRepository;
    }

    @Transactional
    public void depositMoney(Long customerId, BigDecimal amount) throws Exception {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new Exception("Customer not found"));

        customer.setBalanceTRY(customer.getBalanceTRY().add(amount));
        customerRepository.save(customer);
    }

    @Transactional
    public void withdrawMoney(Long customerId, BigDecimal amount) throws Exception {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new Exception("Customer not found"));

        if (customer.getBalanceTRY().compareTo(amount) < 0) {
            throw new Exception("Insufficient balance for withdrawal");
        }

        customer.setBalanceTRY(customer.getBalanceTRY().subtract(amount));
        customerRepository.save(customer);
    }

    public List<Asset> listCustomerAssets(Long customerId) {
        List<Asset> assets = assetRepository.findByCustomerId(customerId);
        return assets;
    }
}
