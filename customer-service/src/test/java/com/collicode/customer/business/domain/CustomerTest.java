package com.collicode.customer.business.domain;

import com.collicode.customer.business.domain.command.CustomerCommand;
import com.collicode.shared.exception.BusinessEntityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldCreateCustomerUsingBuilder() {
        CustomerId id = CustomerId.of(1L);
        Customer customer = Customer.builder()
                .customerId(id)
                .firstName("John")
                .lastName("Doe")
                .otherName("Middle")
                .build();

        assertEquals(id, customer.getCustomerId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("Middle", customer.getOtherName());
    }

    @Test
    void shouldCreateCustomerUsingFromCommand() {
        CustomerCommand cmd = CustomerCommand.builder()
                .firstName("Alice")
                .lastName("Smith")
                .otherName("B.")
                .build();

        CustomerId id = CustomerId.of(2L);
        Customer customer = Customer.from(cmd, id);

        assertEquals(id, customer.getCustomerId());
        assertEquals("Alice", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("B.", customer.getOtherName());
    }

    @Test
    void shouldThrowWhenCustomerIdIsNull() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .otherName("Middle")
                .build();

        BusinessEntityException ex = assertThrows(BusinessEntityException.class, customer::validate);
        assertTrue(ex.getMessage().contains("CustomerId"));
    }

    @Test
    void shouldThrowWhenFirstNameIsMissing() {
        Customer customer = Customer.builder()
                .customerId(CustomerId.of(3L))
                .lastName("Doe")
                .otherName("Middle")
                .firstName(null)
                .build();

        BusinessEntityException ex = assertThrows(BusinessEntityException.class, customer::validate);
        assertTrue(ex.getMessage().contains("FirstName"));
    }

    @Test
    void shouldThrowWhenLastNameIsMissing() {
        Customer customer = Customer.builder()
                .customerId(CustomerId.of(4L))
                .firstName("John")
                .otherName("Middle")
                .lastName("")
                .build();

        BusinessEntityException ex = assertThrows(BusinessEntityException.class, customer::validate);
        assertTrue(ex.getMessage().contains("LastName"));
    }

    @Test
    void shouldPassValidationWhenAllRequiredFieldsArePresent() {
        Customer customer = Customer.builder()
                .customerId(CustomerId.of(5L))
                .firstName("Jane")
                .lastName("Doe")
                .otherName(null)
                .build();

        assertDoesNotThrow(customer::validate);
    }

    @Test
    void shouldCreateNewCustomerWithUpdatedNamesUsingWithNames() {
        Customer original = Customer.builder()
                .customerId(CustomerId.of(6L))
                .firstName("OriginalFirst")
                .lastName("OriginalLast")
                .otherName("OriginalOther")
                .build();

        Customer updated = original.withNames("NewFirst", "NewLast", "NewOther");

        assertEquals(original.getCustomerId(), updated.getCustomerId());
        assertEquals("NewFirst", updated.getFirstName());
        assertEquals("NewLast", updated.getLastName());
        assertEquals("NewOther", updated.getOtherName());
    }

    @Test
    void withNamesShouldKeepOriginalValuesWhenPassedNull() {
        Customer original = Customer.builder()
                .customerId(CustomerId.of(7L))
                .firstName("First")
                .lastName("Last")
                .otherName("Other")
                .build();

        Customer updated = original.withNames(null, null, null);

        assertEquals("First", updated.getFirstName());
        assertEquals("Last", updated.getLastName());
        assertEquals("Other", updated.getOtherName());
    }
}
