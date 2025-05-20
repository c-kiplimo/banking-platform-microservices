package com.collicode.customer.business.domain.adapter.write;

import com.collicode.customer.business.domain.Customer;
import reactor.core.publisher.Mono;

public interface CustomerWriteAdapter {
    Mono<Customer> createCustomer(Customer customer);

    Mono<Customer> updateCustomer(Customer customer);

    Mono<Customer> deleteCustomer(Customer customer);
}
