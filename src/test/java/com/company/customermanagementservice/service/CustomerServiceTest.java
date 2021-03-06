package com.company.customermanagementservice.service;

import com.company.customermanagementservice.model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void saveCustomer_succeeds(){
        Customer customerToBeSaved = new Customer(null, "James", "Doe");
        Customer customerSaved = new Customer(1L, "James", "Doe");

        given(customerRepository.createCustomer(customerToBeSaved)).willReturn(customerSaved);

        Customer customerReturned = customerService.createCustomer(customerToBeSaved);
        assertThat(customerReturned.getId()).isEqualTo(1L);
        assertThat(customerReturned.getFirstName()).isEqualTo("James");
    }

    @Test
    public void deleteCustomer_succeeds(){

        Long customerId = 10L;

        given(customerRepository.findById(customerId)).willReturn(
                Optional.of(new Customer(10L,"James", "Does")));

        Mockito.doNothing().when(customerRepository).delete(customerId);

        customerService.deleteCustomer(10L);

        verify(customerRepository, times(1)).delete(customerId);
        verify(customerRepository, times(1)).findById(customerId);
    }


    @Test(expected = EntityNotFoundException.class)
    public void deleteCustomer_throwsNotFoundException(){
        given(customerRepository.findById(100L)).willReturn(Optional.empty());

        customerService.deleteCustomer(100L);
    }
}
