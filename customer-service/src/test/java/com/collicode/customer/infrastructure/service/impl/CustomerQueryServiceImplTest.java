package com.collicode.customer.infrastructure.service.impl;

import com.collicode.customer.infrastructure.repository.model.read.CustomerReadModel;
import com.collicode.customer.infrastructure.repository.read.CustomerReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerQueryServiceImplTest {

    private CustomerReadRepository customerReadRepository;
    private CustomerQueryServiceImpl customerQueryService;

    @BeforeEach
    void setup() {
        customerReadRepository = Mockito.mock(CustomerReadRepository.class);
        customerQueryService = new CustomerQueryServiceImpl(customerReadRepository);
    }

    @Test
    void fetchCustomer_ShouldReturnCustomerReadModel() {
        // Arrange
        CustomerReadModel customer = CustomerReadModel.builder()
                .recordId(1L)
                .firstName("John")
                .lastName("Doe")
                .otherName("M")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        when(customerReadRepository.fetchCustomerById(1L)).thenReturn(Mono.just(customer));

        // Act & Assert
        StepVerifier.create(customerQueryService.fetchCustomer(1L))
                .expectNextMatches(c -> c.getFirstName().equals("John") && c.getRecordId() == 1L)
                .verifyComplete();

        verify(customerReadRepository, times(1)).fetchCustomerById(1L);
    }

    @Test
    void fetchAllCustomers_ShouldReturnFluxOfCustomers() {
        // Arrange
        CustomerReadModel customer1 = CustomerReadModel.builder()
                .recordId(1L)
                .firstName("Alice")
                .lastName("Smith")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .build();

        CustomerReadModel customer2 = CustomerReadModel.builder()
                .recordId(2L)
                .firstName("Bob")
                .lastName("Brown")
                .createdAt(LocalDateTime.now().minusDays(8))
                .updatedAt(LocalDateTime.now().minusDays(3))
                .build();

        when(customerReadRepository.fetchAllCustomers(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Flux.just(customer1, customer2));

        // Act & Assert
        StepVerifier.create(customerQueryService.fetchAllCustomers("Alice", LocalDate.now().minusDays(30), LocalDate.now()))
                .expectNextCount(2)
                .verifyComplete();

        verify(customerReadRepository, times(1))
                .fetchAllCustomers("Alice", LocalDate.now().minusDays(30), LocalDate.now());
    }
}
