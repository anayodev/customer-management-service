package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customerToBeCreated);

    List<Customer> findAllCustomers();

    void deleteCustomer(Long customerId);
}
