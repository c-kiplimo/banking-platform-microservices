package com.collicode.customer.business.domain.service.impl;

import com.collicode.customer.business.domain.Customer;
import com.collicode.customer.business.domain.CustomerId;
import com.collicode.customer.business.domain.adapter.write.CustomerWriteAdapter;
import com.collicode.customer.business.domain.command.CustomerCommand;
import com.collicode.customer.business.domain.service.CustomerValidationService;
import com.collicode.shared.domain.service.SequenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private static final String ENTITYNAME = "CUSTOMER";

    private CustomerValidationService customerValidationService;
    private CustomerWriteAdapter customerWriteAdapter;
    private SequenceRepository<CustomerId, Long, Long> sequenceRepository;

    private CustomerServiceImpl customerService;

    @BeforeEach
    void setup() {
        customerValidationService = mock(CustomerValidationService.class);
        customerWriteAdapter = mock(CustomerWriteAdapter.class);
        sequenceRepository = mock(SequenceRepository.class);

        customerService = new CustomerServiceImpl(customerValidationService, customerWriteAdapter, sequenceRepository);
    }

    @Test
    void createCustomer_shouldCreateWithInitialId_whenNoPreviousIdExists() {
        CustomerCommand command = CustomerCommand.builder()
                .firstName("John")
                .lastName("Doe")
                .otherName("M")
                .build();

        CustomerId initialId = CustomerId.initialId();

        // Simulate no existing sequence ID
        when(sequenceRepository.findPreviousId(anyString())).thenReturn(Mono.empty());
        when(sequenceRepository.insertId(anyString(), anyLong(), anyLong())).thenReturn(Mono.empty());

        // No duplicate customer found
        when(customerValidationService.checkDuplicateCustomer(anyLong())).thenReturn(Mono.empty());

        // Mock creation adapter to just return passed customer
        when(customerWriteAdapter.createCustomer(any(Customer.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Customer> result = customerService.createCustomer(command);

        StepVerifier.create(result)
                .assertNext(customer -> {
                    assertEquals(initialId.getCurrentId(), customer.getCustomerId().getCurrentId());
                    assertEquals(command.getFirstName(), customer.getFirstName());
                    assertEquals(command.getLastName(), customer.getLastName());
                    assertEquals(command.getOtherName(), customer.getOtherName());
                })
                .verifyComplete();

        verify(sequenceRepository).insertId(eq(ENTITYNAME), eq(initialId.getCurrentId()), eq(initialId.getFactor()));
    }

    @Test
    void updateCustomer_shouldUpdateCustomerAlias() {
        long customerIdValue = 123L;
        CustomerCommand command = CustomerCommand.builder()
                .customerId(customerIdValue)
                .firstName("Jane")
                .lastName("Smith")
                .otherName("L")
                .build();

        Customer existingCustomer = Customer.builder()
                .customerId(CustomerId.of(customerIdValue, 1L))
                .firstName("OldFirst")
                .lastName("OldLast")
                .otherName("OldOther")
                .build();

        Customer updatedCustomer = existingCustomer.withNames(command.getFirstName(), command.getLastName(), command.getOtherName());

        when(customerValidationService.findCustomerByCustomerId(customerIdValue))
                .thenReturn(Mono.just(existingCustomer));

        when(customerWriteAdapter.updateCustomer(any(Customer.class)))
                .thenReturn(Mono.just(updatedCustomer));

        Mono<Customer> result = customerService.updateCustomer(command);

        StepVerifier.create(result)
                .assertNext(customer -> {
                    assertEquals(command.getFirstName(), customer.getFirstName());
                    assertEquals(command.getLastName(), customer.getLastName());
                    assertEquals(command.getOtherName(), customer.getOtherName());
                })
                .verifyComplete();

        verify(customerValidationService).findCustomerByCustomerId(customerIdValue);
        verify(customerWriteAdapter).updateCustomer(any(Customer.class));
    }


    @Test
    void identity_shouldReturnInitialCustomerId_whenNoPreviousExists() {
        CustomerId initialId = CustomerId.initialId();

        when(sequenceRepository.findPreviousId(ENTITYNAME)).thenReturn(Mono.empty());
        when(sequenceRepository.insertId(ENTITYNAME, initialId.getCurrentId(), initialId.getFactor())).thenReturn(Mono.empty());

        StepVerifier.create(customerService.identity())
                .assertNext(id -> {
                    assertEquals(initialId.getCurrentId(), id.getCurrentId());
                    assertEquals(initialId.getFactor(), id.getFactor());
                })
                .verifyComplete();

        verify(sequenceRepository).findPreviousId(ENTITYNAME);
        verify(sequenceRepository).insertId(ENTITYNAME, initialId.getCurrentId(), initialId.getFactor());
        verify(sequenceRepository, never()).updateId(anyString(), anyLong());
    }

}
