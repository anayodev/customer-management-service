# Customer Management Servcice

An implementation of the customer management service RESTful api for managing customers. it supports the below operations

* Operation 1: Add a Customer 
* Operation 2: Remove a Customer, given their ID 
* Operation 3: List all Customers 

The application repo is instantiated with two customers
``` 
[
  {
    "id": 1,
    "firstName": "John",
    "secondName": "Doe"
  },
  {
    "id": 2,
    "firstName": "Mary",
    "secondName": "Doe"
  }
]
``` 
#### Operation 1 : Add Customer
``` 
Example Request - Add a customer
curl --location --request POST "http://localhost:8091/customer-project/rest/customer/json" \
  --header "Content-Type: application/json" \
  --data "{
    \"firstName\": \"Bill\",
    \"secondName\": \"Gates\"
}"
Example Response
201 Created
{
  "id": 3,
  "firstName": "Bill",
  "secondName": "Gates"
}
```
#### Operation 2: Remove a Customer, given their ID 
```
Example Request - Remove a customer
curl --location --request DELETE "http://localhost:8091/customer-project/rest/customer/json/1" \
  --header "Content-Type: application/json" \
  --data ""
Example Response
200 OK
{
  "message": "Customer with id:2 successfully deleted"
}
```
#### Operation 3: List all Customers
```
Example Request - Get all customers
curl --location --request GET "http://localhost:8091/customer-project/rest/customer/json" \
  --data ""
Example Response
[
  {
    "id": 1,
    "firstName": "John",
    "secondName": "Doe"
  },
  {
    "id": 2,
    "firstName": "Mary",
    "secondName": "Doe"
  }
]
```

### Prerequisites

Things to install in order to run the tests or build the application

* Java SDK 8 - Download, install and set java home for linux and windows using the instructions here - https://docs.oracle.com/cd/E19182-01/821-0917/inst_jdk_javahome_t/index.html
* Maven version 3.5 and above or use the maven wrappper accompanying the project. See https://maven.apache.org/install.html for installing maven and *Using the application* section for instructions on using maven wrapper
* An integrated development environment (IDE) like Eclipse or Intellij for reviewing the source code
* Git - Download and install git, see - https://git-scm.com/downloads. Git is required should the evaluator of the coding solution wish to see the commit history that ideally captured TDD while doing the implementation

## Built With
* [Maven](https://maven.apache.org/) - Dependency Management
