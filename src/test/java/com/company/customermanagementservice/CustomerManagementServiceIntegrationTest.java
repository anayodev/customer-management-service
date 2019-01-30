package com.company.customermanagementservice;

import com.company.customermanagementservice.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		assertThat(response.getBody().size()).isGreaterThanOrEqualTo(2);
		assertThat(response.getBody().get(0).getId()).isEqualTo(1);
		assertThat(response.getBody().get(0).getFirstName()).isEqualTo("John");
		assertThat(response.getBody().get(0).getSecondName()).isEqualTo("Doe");
	}
    @Test
    public void creatingCustomer_succeeds(){
        Customer customer = new Customer(null, "Mary", "Poppins");
        ResponseEntity<Customer> createdCustomer = postCustomer(customer);

        assertThat(createdCustomer.getStatusCode().value()).isEqualTo(201);
        assertThat(createdCustomer.getBody()).isNotNull();
        assertThat(createdCustomer.getBody().getFirstName()).isEqualTo("Mary");
        assertThat(createdCustomer.getBody().getId()).isNotNull();
    }
    
	@Test
	public void postingBlankCustomerFields_throwsBadRequest() throws JsonProcessingException {
		Customer customerToAdd = new Customer(null,"", "");
		Customer customerToAddForSubErrorMessage = new Customer(null,"", "AAA");

		//posts
		ResponseEntity<String> createCustomerResponse = testRestTemplate.exchange(targetUrl, HttpMethod.POST,
				buildHttpEntity(customerToAdd, getHeadersValues())
				, String.class);

		ResponseEntity<String> createCustomerResponse2 = testRestTemplate.exchange(targetUrl, HttpMethod.POST,
				buildHttpEntity(customerToAddForSubErrorMessage, getHeadersValues())
				, String.class);

		//verifications
		assertThat(createCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(JsonPath.parse(createCustomerResponse.getBody()).read("$.subErrors.length()", Integer.class))
				.isEqualTo(2);
		assertThat(JsonPath.parse(createCustomerResponse2.getBody())
				.read("$.subErrors[0].message", String.class))
				.isEqualTo("First name should have between 1 and 60 characters");

	}

	@Test
	public void postingExistingCustomerId_throwsBadRequest() throws JsonProcessingException{
		// Customer 1L, James, Doe already exists from start up initialization of repository
		// Two James Doe will be allowed be allowed as names are not unique, but same id, first name,
		// second name will not be allowed
		Customer customerToAdd = new Customer(null, "James", "Doe");

		ResponseEntity<Customer> customerToAddResponse = postCustomer(customerToAdd);

		Customer repeatedCustomer = new Customer(1L, "James", "Doe");

		ResponseEntity<String> repeatedCreateCustomerResponse = testRestTemplate.exchange(targetUrl, HttpMethod.POST,
				buildHttpEntity(repeatedCustomer, getHeadersValues())
				, String.class);

		//Assert same first name and second name is allowed
		assertThat(customerToAddResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		//Assert repeated id, first name and second name is not allowed
		assertThat(repeatedCreateCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(JsonPath.parse(repeatedCreateCustomerResponse.getBody())
				.read("$.debugMessage", String.class))
				.isEqualTo("Constraint Violation - Customer with id:1 already exists");
	}
    
	@Test
	public void deletingCustomer_succeeds() throws JsonProcessingException {
		Customer customerToAdd = new Customer(null,"James", "Doe");

		//perform post of object
		ResponseEntity<Customer> customerResponseEntity = postCustomer(customerToAdd);

		long customerId = customerResponseEntity.getBody().getId();
		Customer customerCreated = customerResponseEntity.getBody();

		//perform delete of object
		ResponseEntity<String> deleteResponse = testRestTemplate.exchange
				(String.format("%s/%d",targetUrl,customerId), HttpMethod.DELETE,
				buildHttpEntity("", getHeadersValues()), String.class);

		//start verification
		ResponseEntity<List<Customer>> response = testRestTemplate.exchange(targetUrl,HttpMethod.GET,null,
				new ParameterizedTypeReference<List<Customer>>() {});

		assertThat(deleteResponse.getBody()).isEqualTo(
				String.format("{\"message\":\"Customer with id:%d successfully deleted\"}", customerId));

		assertThat(deleteResponse.getStatusCode().value()).isEqualTo(200);

		assertThat(response.getBody().contains(customerCreated)).isFalse();
	}

	@Test
	public void deletingCustomer_FailsWhenCustomerIdNotFound() throws JsonProcessingException {
		//perform delete of object
		ResponseEntity<String> deleteResponse = testRestTemplate.exchange(targetUrl +"/100", HttpMethod.DELETE,
				buildHttpEntity("", getHeadersValues())
				, String.class);

		assertThat(deleteResponse.getStatusCode().value()).isEqualTo(404);
		assertThat(JsonPath.parse(deleteResponse.getBody())
				.read("$.message", String.class))
				.isEqualTo("Customer cannot be deleted");
		assertThat(JsonPath.parse(deleteResponse.getBody())
				.read("$.debugMessage", String.class))
				.isEqualTo("Customer with Id 100 does not exist");
	}
	

	private ResponseEntity<Customer> postCustomer(Customer customer){

		return testRestTemplate.exchange(targetUrl,HttpMethod.POST,
				new HttpEntity<>(customer) , new ParameterizedTypeReference<Customer>() {});
	}

	private HttpEntity<String> buildHttpEntity(Object requestBodyObj, Map<String, String> headersValues )
			throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();

		headersValues.forEach(headers::add);

		String requestBody = new ObjectMapper().writeValueAsString(requestBodyObj);

		return new HttpEntity<>(requestBody, headers);
	}
	private Map<String, String> getHeadersValues(){
		return Stream.of(new AbstractMap.SimpleEntry<>("Content-Type", MediaType.APPLICATION_JSON_VALUE))
				.collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
	}
}