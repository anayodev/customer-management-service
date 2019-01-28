package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerRepositoryTest {

    @Test
    public void findAll_returnsAllCustomers(){
        /** repository has two records
         * 1L John Doe, 2L Mary Doe
         */
        CustomerRepository customerRepository = new CustomerRepositoryImpl();

        List<Customer> allReturnedCustomers = customerRepository.findAll();
        assertThat(allReturnedCustomers.size()).isEqualTo(2);
        assertThat(allReturnedCustomers.get(0).getFirstName()).isEqualTo("John");
        assertThat(allReturnedCustomers.get(0).getSecondName()).isEqualTo("Doe");
    }
}
