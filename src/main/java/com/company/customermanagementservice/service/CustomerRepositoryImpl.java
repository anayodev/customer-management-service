package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private Map<Long, Customer> customers;

    private final AtomicLong lastIdHolder;

    public CustomerRepositoryImpl() {

        lastIdHolder = new AtomicLong();

        this.customers = Stream.of(
                new Customer(getNextId(), "John", "Doe"),
                new Customer(getNextId(), "Mary", "Doe"))
                .collect(Collectors.toConcurrentMap(Customer::getId, customer -> customer));
    }

    private Long getNextId(){
        return lastIdHolder.incrementAndGet();
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public Customer createCustomer(Customer customerToBeCreated) {
        Customer newCustomer = new Customer(getNextId(), customerToBeCreated.getFirstName(),
                customerToBeCreated.getSecondName());

        customers.put(newCustomer.getId(), newCustomer);

        return newCustomer;
    }

    @Override
    public void delete(Long customerId) {
        customers.remove(customerId);
    }
}
