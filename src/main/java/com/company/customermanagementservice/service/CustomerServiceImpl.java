package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    public CustomerServiceImpl(CustomerRepository customerRepository) {
    }

    @Override
    public List<Customer> findAllCustomers() {
        return null;
    }
}
