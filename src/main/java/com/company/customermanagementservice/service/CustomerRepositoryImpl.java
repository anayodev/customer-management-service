package com.company.customermanagementservice.service;

import com.company.customermanagementservice.exception.DataIntegrityViolationException;
import com.company.customermanagementservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private Map<Long, Customer> customers;

    private final AtomicLong lastIdHolder;

    @Autowired
    private Validator validator;

    public CustomerRepositoryImpl() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

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

        Customer newCustomer;

        validateCustomer(customerToBeCreated);

        if(Objects.nonNull(customerToBeCreated.getId())){
            if(Objects.nonNull(customers.get(customerToBeCreated.getId()))){
                throw new DataIntegrityViolationException(
                        String.format("Constraint Violation - Customer with id:%d already exists",
                                customerToBeCreated.getId()));
            }else{
                newCustomer = new Customer(customerToBeCreated.getId(), customerToBeCreated.getFirstName(),
                        customerToBeCreated.getSecondName());
            }
        }else {

            newCustomer = new Customer(getNextId(), customerToBeCreated.getFirstName(),
                    customerToBeCreated.getSecondName());
        }

        customers.put(newCustomer.getId(), newCustomer);

        return newCustomer;
    }

    private void validateCustomer(Customer customerToBeCreated) {
        Set<ConstraintViolation<Customer>> violations = validator.validate(customerToBeCreated);

        if (!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }

    }


    @Override
    public void delete(Long customerId) {
        customers.remove(customerId);
    }

    @Override
    public Optional<Customer> findById(long customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }
}
