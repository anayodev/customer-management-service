package com.company.customermanagementservice;

import com.company.customermanagementservice.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerManagementServiceIntegrationTest {
	@Autowired
	private TestRestTemplate testRestTemplate;

	private String targetUrl = "/customer-project/rest/customer/json";

	@Test
	public void getCustomers_returnsAllCustomers(){
        /** repository is initialized with two customer records
         * 1L John Doe, 2L Mary Doe
         */
		ResponseEntity<List<Customer>> response = testRestTemplate.exchange(targetUrl, HttpMethod.GET,null,
				new ParameterizedTypeReference<List<Customer>>() {});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().size()).isEqualTo(2);
		assertThat(response.getBody().get(0).getId()).isEqualTo(1);
		assertThat(response.getBody().get(0).getFirstName()).isEqualTo("John");
		assertThat(response.getBody().get(0).getSecondName()).isEqualTo("Doe");
	}
    @Test
    public void creatingCustomer_succeeds(){
        Customer customer = new Customer(null, "Mary", "Poppins");
        ResponseEntity<Customer> createdCustomer = testRestTemplate.exchange(targetUrl,HttpMethod.POST,
                new HttpEntity<>(customer) , new ParameterizedTypeReference<Customer>() {});

        assertThat(createdCustomer.getStatusCode()).isEqualTo(201);
        assertThat(createdCustomer.getBody()).isNotNull();
        assertThat(createdCustomer.getBody().getFirstName()).isEqualTo("Mary");
        assertThat(createdCustomer.getBody().getId()).isNotNull();
    }
	
}