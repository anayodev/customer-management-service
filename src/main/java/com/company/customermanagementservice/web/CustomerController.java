package com.company.customermanagementservice.web;

import com.company.customermanagementservice.model.Customer;
import com.company.customermanagementservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-project/rest/customer/json")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Customer> listCustomers(){
        return customerService.findAllCustomers();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody Customer customer){
        return customerService.createCustomer(customer);
    }

    @DeleteMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return String.format("{\"message\":\"Customer with id:%d successfully deleted\"}", id);
    }
}
