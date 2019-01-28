package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @Before
    public void setUp() {
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    public void findCustomers_returnsAllCustomer() {
        Customer customer1 = new Customer(1L, "John", "Doe");
        Customer customer2 = new Customer(1L, "Mary", "Doe");

        List<Customer> allCustomers = Stream.of(customer1, customer2).collect(Collectors.toList());
        given(customerRepository.findAll()).willReturn(allCustomers);

        List<Customer> allReturnedCustomers = customerService.findAllCustomers();
        assertThat(allReturnedCustomers.size()).isEqualTo(2);
        assertThat(allReturnedCustomers.get(0).getFirstName()).isEqualTo("John");
        assertThat(allReturnedCustomers.get(0).getSecondName()).isEqualTo("Doe");
    }
}
