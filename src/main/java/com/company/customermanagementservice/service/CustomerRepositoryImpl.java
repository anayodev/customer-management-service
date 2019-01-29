package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private Map<Long, Customer> customers;

    public CustomerRepositoryImpl() {
        this.customers = Stream.of(
                new Customer(1L, "John", "Doe"),
                new Customer(2L, "Mary", "Doe"))
                .collect(Collectors.toConcurrentMap(Customer::getId, customer -> customer));
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public Customer createCustomer(Customer customerToBeSaved) {
        return null;
    }
}
