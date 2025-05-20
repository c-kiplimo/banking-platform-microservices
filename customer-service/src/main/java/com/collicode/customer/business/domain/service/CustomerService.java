package com.collicode.customer.business.domain.service;

import com.collicode.customer.business.domain.Customer;
import com.collicode.customer.business.domain.command.CustomerCommand;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<Customer> createCustomer(CustomerCommand customerCommand);

    Mono<Customer> updateCustomer(CustomerCommand customerCommand);

    Mono<Customer> deleteCustomer(long customerId);
}
