package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customerToBeCreated) {

        return customerRepository.createCustomer(customerToBeCreated);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteCustomer(Long customerId) {

        Optional<Customer> customer = customerRepository.findById(customerId);

        if(customer.isPresent()){
            customerRepository.delete(customerId);
        }else{
            throw new EntityNotFoundException(String.format("Customer with Id %s does not exist", customerId));
        }
    }
}
