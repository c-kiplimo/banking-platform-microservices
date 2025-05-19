package com.collicode.customer.business.domain.service.impl;

import com.collicode.customer.business.domain.Customer;
import com.collicode.customer.business.domain.adapter.read.CustomerReadAdapter;
import com.collicode.customer.business.domain.service.CustomerValidationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.collicode.customer.business.domain.constants.CustomerConstants.ENTITYNAME;
import static com.collicode.shared.exception.BusinessEntityException.entityNotFoundException;
import static com.collicode.shared.exception.BusinessException.duplicateKeyException;

@Service
public class CustomerValidationServiceImpl implements CustomerValidationService {
    private final CustomerReadAdapter customerReadAdapter;

    public CustomerValidationServiceImpl(CustomerReadAdapter customerReadAdapter) {
        this.customerReadAdapter = customerReadAdapter;
    }

    @Override
    public Mono<Customer> findCustomerByCustomerId(long customerId) {
        return customerReadAdapter.findCustomerByCustomerId(customerId)
                .switchIfEmpty(Mono.error(entityNotFoundException(ENTITYNAME)));
    }

    @Override
    public Mono<Customer> checkDuplicateCustomer(long customerId) {
        return customerReadAdapter.findCustomerByCustomerId(customerId)
                .flatMap(customer -> Mono.error(duplicateKeyException(ENTITYNAME)));
    }
}
