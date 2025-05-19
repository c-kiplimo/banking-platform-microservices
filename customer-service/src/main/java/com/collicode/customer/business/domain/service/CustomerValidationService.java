package com.collicode.customer.business.domain.service;

import com.collicode.customer.business.domain.Customer;
import reactor.core.publisher.Mono;

public interface CustomerValidationService {
    Mono<Customer> findCustomerByCustomerId(long customerId);

    Mono<Customer> checkDuplicateCustomer(long customerId);

}
