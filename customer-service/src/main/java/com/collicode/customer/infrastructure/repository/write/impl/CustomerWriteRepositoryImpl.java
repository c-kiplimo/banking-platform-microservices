package com.collicode.customer.infrastructure.repository.write.impl;

import com.collicode.customer.infrastructure.repository.model.write.CustomerWriteModel;
import com.collicode.customer.infrastructure.repository.write.CustomerWriteRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CustomerWriteRepositoryImpl implements CustomerWriteRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public CustomerWriteRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<CustomerWriteModel> createCustomer(CustomerWriteModel customerWriteModel) {
        return r2dbcEntityTemplate.insert(customerWriteModel);
    }

    @Override
    public Mono<CustomerWriteModel> deleteCustomer(CustomerWriteModel customerWriteModel) {
        return r2dbcEntityTemplate.delete(customerWriteModel);
    }

    @Override
    public Mono<CustomerWriteModel> updateCustomer(CustomerWriteModel customerWriteModel) {
        return r2dbcEntityTemplate.update(customerWriteModel);
    }
}
