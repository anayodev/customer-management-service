package com.company.customermanagementservice.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Customer {

    private Long id;

    @NotNull(message ="First name should have between 1 and 60 characters")
    @Size(min = 1, max = 60, message ="First name should have between 1 and 60 characters" )
    private String firstName;

    @NotNull(message ="Second name should have between 1 and 60 characters")
    @Size(min = 1, max = 60, message ="Second name should have between 1 and 60 characters")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                Objects.equals(firstName, customer.firstName) &&
                Objects.equals(secondName, customer.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName);
    }
}
