package com.collicode.customer.infrastructure.repository.read;

import com.collicode.customer.infrastructure.repository.model.read.CustomerReadModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface CustomerReadRepository {
    Mono<CustomerReadModel> fetchCustomerById(long customerId);

    Flux<CustomerReadModel> fetchAllCustomers(String name, LocalDate startDate, LocalDate endDate);

}
