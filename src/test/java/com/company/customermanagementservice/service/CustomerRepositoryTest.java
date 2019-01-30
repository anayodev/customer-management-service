package com.company.customermanagementservice.service;

import com.company.customermanagementservice.exception.DataIntegrityViolationException;
import com.company.customermanagementservice.model.Customer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerRepositoryTest {
    /** repository has two records
     * 1L John Doe, 2L Mary Doe
     */

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test(expected = ConstraintViolationException.class)
    public void postingBlankCustomerFields_throwsException() {
        Customer customer = new Customer(null,"", "");

        CustomerRepository customerRepository = new CustomerRepositoryImpl();

        customerRepository.createCustomer(customer);
    }
    @Test
    public void postingExistingCustomerId_throwsException() {


        expectedException.expect(DataIntegrityViolationException.class);

        expectedException.expectMessage("Constraint Violation - Customer with id:3 already exists");

        Customer customer = new Customer(null, "John", "Adams");

        Customer sameCustomer = new Customer(3L,"John", "Adams");

        CustomerRepository customerRepository = new CustomerRepositoryImpl();

        customerRepository.createCustomer(customer);
        customerRepository.createCustomer(sameCustomer);
    }
}
