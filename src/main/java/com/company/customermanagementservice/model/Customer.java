package com.company.customermanagementservice.model;

public class Customer {

    private Long id;

    private String firstName;

    private String secondName;

    public Customer(Long id, String firstName, String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }
}
