package com.kaya.brokerage_backend.brokerage_backend.controller;

import com.kaya.brokerage_backend.brokerage_backend.entity.Asset;
import com.kaya.brokerage_backend.brokerage_backend.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/deposit")
    public void depositMoney(
            @RequestParam Long customerId,
            @RequestParam BigDecimal amount
    ) throws Exception {
        customerService.depositMoney(customerId, amount);
    }

    @PostMapping("/withdraw")
    public void withdrawMoney(
            @RequestParam Long customerId,
            @RequestParam String iban,
            @RequestParam BigDecimal amount
    ) throws Exception {
        customerService.withdrawMoney(customerId, iban, amount);
    }

    @GetMapping("/assets")
    public List<Asset> listAssets(
            @RequestParam Long customerId,
            @RequestParam(required = false) String assetNameFilter
    ) {
        return customerService.listCustomerAssets(customerId, assetNameFilter);
    }
}