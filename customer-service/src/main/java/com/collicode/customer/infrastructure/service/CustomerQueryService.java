package com.collicode.customer.infrastructure.service;

import com.collicode.customer.infrastructure.repository.model.read.CustomerReadModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface CustomerQueryService {
    Mono<CustomerReadModel> fetchCustomer(long customerId);

    Flux<CustomerReadModel> fetchAllCustomers(String name, LocalDate startDate, LocalDate endDate);
}
