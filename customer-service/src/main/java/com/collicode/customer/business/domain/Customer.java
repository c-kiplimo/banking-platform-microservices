package com.collicode.customer.business.domain;

import com.collicode.customer.business.domain.command.CustomerCommand;
import com.collicode.shared.domain.BusinessEntity;
import lombok.Getter;

import java.util.Objects;

import static com.collicode.customer.business.domain.constants.CustomerConstants.ENTITYNAME;
import static com.collicode.shared.exception.BusinessEntityException.missingKeyException;
import static com.collicode.shared.util.BusinessValidation.ifHasNoText;
import static com.collicode.shared.util.BusinessValidation.ifTrue;

@Getter
public class Customer extends BusinessEntity {
    private final CustomerId customerId;
    private final String firstName;
    private final String lastName;
    private final String otherName;

    private Customer(CustomerBuilder customerBuilder) {
        this.customerId = customerBuilder.customerId;
        this.firstName = customerBuilder.firstName;
        this.lastName = customerBuilder.lastName;
        this.otherName = customerBuilder.otherName;
    }

    public static Customer from(CustomerCommand customerCommand, CustomerId customerId) {
        return Customer.builder()
                .customerId(customerId)
                .firstName(customerCommand.getFirstName())
                .lastName(customerCommand.getLastName())
                .otherName(customerCommand.getOtherName())
                .build();
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    @Override
    public void validate() {
        ifTrue(Objects.isNull(this.customerId), missingKeyException(ENTITYNAME, "CustomerId"));
        ifHasNoText(this.firstName, missingKeyException(ENTITYNAME, "FirstName"));
        ifHasNoText(this.lastName, missingKeyException(ENTITYNAME, "LastName"));
    }

    public Customer withNames(String firstName, String lastName, String otherName) {
        return Customer.builder()
                .customerId(this.customerId)
                .firstName(firstName != null ? firstName : this.firstName)
                .lastName(lastName != null ? lastName : this.lastName)
                .otherName(otherName != null ? otherName : this.otherName)
                .build();
    }


    public static class CustomerBuilder {
        private CustomerId customerId;
        private String firstName;
        private String lastName;
        private String otherName;

        public CustomerBuilder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public CustomerBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerBuilder otherName(String otherName) {
            this.otherName = otherName;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
