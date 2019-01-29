package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class CustomerRepositoryTest {
    /** repository has two records
     * 1L John Doe, 2L Mary Doe
     */

    @Test
    public void findAll_returnsAllCustomers(){

        CustomerRepository customerRepository = new CustomerRepositoryImpl();

        List<Customer> allReturnedCustomers = customerRepository.findAll();

        assertThat(allReturnedCustomers.size()).isEqualTo(2);
        assertThat(allReturnedCustomers.get(0).getFirstName()).isEqualTo("John");
        assertThat(allReturnedCustomers.get(0).getSecondName()).isEqualTo("Doe");
    }

    @Test
    public void saveCustomer_succeeds(){
        // Customer ids are meant to be sequential, with 2 customers in the instantiated object,
        // the next id is meant to be 3

        CustomerRepository customerRepository = new CustomerRepositoryImpl();

        Customer customerToBeSaved = new Customer(null, "James", "Doe");

        Customer customerReturned = customerRepository.createCustomer(customerToBeSaved);

        assertThat(customerReturned.getId()).isEqualTo(3L);
        assertThat(customerReturned.getFirstName()).isEqualTo("James");
    }

    @Test
    public void deleteCustomer_succeeds(){

        CustomerRepository customerRepository = new CustomerRepositoryImpl();

        Long customerId = 1L;

        customerRepository.delete(customerId);

        assertThat(customerRepository.findAll().size()).isEqualTo(1);
        assertThat(customerRepository.findAll().get(0).getFirstName()).isEqualTo("Mary");
    }
}
