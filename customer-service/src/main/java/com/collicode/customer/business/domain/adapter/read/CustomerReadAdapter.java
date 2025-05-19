package com.collicode.customer.business.domain.adapter.read;

import com.collicode.customer.business.domain.Customer;
import reactor.core.publisher.Mono;

public interface CustomerReadAdapter {
    Mono<Customer> findCustomerByCustomerId(long customerId);
}
