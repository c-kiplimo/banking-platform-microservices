package com.collicode.customer.infrastructure.service.impl;

import com.collicode.customer.infrastructure.repository.model.read.CustomerReadModel;
import com.collicode.customer.infrastructure.repository.read.CustomerReadRepository;
import com.collicode.customer.infrastructure.service.CustomerQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class CustomerQueryServiceImpl implements CustomerQueryService {
    private final CustomerReadRepository customerReadRepository;

    public CustomerQueryServiceImpl(CustomerReadRepository customerReadRepository) {
        this.customerReadRepository = customerReadRepository;
    }

    @Override
    public Mono<CustomerReadModel> fetchCustomer(long customerId) {
        return customerReadRepository.fetchCustomerById(customerId);
    }

    @Override
    public Flux<CustomerReadModel> fetchAllCustomers(String name, LocalDate startDate, LocalDate endDate) {
        return customerReadRepository.fetchAllCustomers(name, startDate, endDate);
    }
}
