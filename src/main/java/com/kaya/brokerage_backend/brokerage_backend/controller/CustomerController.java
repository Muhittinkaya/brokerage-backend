package com.kaya.brokerage_backend.brokerage_backend.controller;

import com.kaya.brokerage_backend.brokerage_backend.entity.Asset;
import com.kaya.brokerage_backend.brokerage_backend.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Deposit", description = "Deposit money from customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deposit",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/deposit")
    public void depositMoney(
            @RequestParam Long customerId,
            @RequestParam BigDecimal amount
    ) throws Exception {
        customerService.depositMoney(customerId, amount);
    }

    @Operation(summary = "Withdraw", description = "Withdraw money from customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully withdrawed", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/withdraw")
    public void withdrawMoney(
            @RequestParam Long customerId,
            @RequestParam BigDecimal amount
    ) throws Exception {
        customerService.withdrawMoney(customerId, amount);
    }

    @Operation(summary = "List assets", description = "Retrieve a list of all assets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/assets")
    public List<Asset> listAssets(
            @RequestParam Long customerId
    ) {
        return customerService.listCustomerAssets(customerId);
    }
}