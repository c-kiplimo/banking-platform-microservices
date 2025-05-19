package com.collicode.customer.business.application.queryservice;

import com.collicode.customer.business.domain.Customer;
import com.collicode.customer.business.domain.CustomerId;
import com.collicode.customer.business.domain.adapter.read.CustomerReadAdapter;
import com.collicode.customer.infrastructure.repository.read.CustomerReadRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerReadService implements CustomerReadAdapter {
    private final CustomerReadRepository customerReadRepository;

    public CustomerReadService(CustomerReadRepository customerReadRepository) {
        this.customerReadRepository = customerReadRepository;
    }

    @Override
    public Mono<Customer> findCustomerByCustomerId(long customerId) {
        return customerReadRepository.fetchCustomerById(customerId)
                .map(customerReadModel -> Customer.builder()
                        .customerId(CustomerId.of(customerReadModel.getRecordId()))
                        .firstName(customerReadModel.getFirstName())
                        .lastName(customerReadModel.getLastName())
                        .otherName(customerReadModel.getOtherName())
                        .build());
    }
}
