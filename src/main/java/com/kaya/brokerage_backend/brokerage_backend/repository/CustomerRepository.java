package com.kaya.brokerage_backend.brokerage_backend.repository;

import com.kaya.brokerage_backend.brokerage_backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}
