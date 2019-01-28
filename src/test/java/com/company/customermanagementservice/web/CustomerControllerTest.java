package com.company.customermanagementservice.web;

import com.company.customermanagementservice.model.Customer;
import com.company.customermanagementservice.service.CustomerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private String targetUrl = "/Customer-project/rest/Customer/json";

    @Test
    public void getCustomers_returnsAllCustomers() throws Exception{

        Customer customer1 = new Customer(1L, "John", "Doe");
        Customer customer2 = new Customer(1L, "Mary", "Doe");

        List<Customer> allCustomers = Stream.of(customer1, customer2).collect(Collectors.toList());

        given(customerService.findAllCustomers()).willReturn(allCustomers);

        mockMvc.perform(MockMvcRequestBuilders.get(targetUrl))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].secondName").value("Doe"));
    }
}
