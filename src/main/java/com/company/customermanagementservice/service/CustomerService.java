package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAllCustomers();
}
