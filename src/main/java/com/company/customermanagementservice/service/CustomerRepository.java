package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    List<Customer> findAll();

    Customer createCustomer(Customer customerToBeCreated);

    void delete(Long customerId);

    Optional<Customer> findById(long customerId);
}
