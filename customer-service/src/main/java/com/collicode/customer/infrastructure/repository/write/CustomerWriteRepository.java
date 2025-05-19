package com.collicode.customer.infrastructure.repository.write;

import com.collicode.customer.infrastructure.repository.model.write.CustomerWriteModel;
import reactor.core.publisher.Mono;

public interface CustomerWriteRepository {
    Mono<CustomerWriteModel> createCustomer(CustomerWriteModel customerWriteModel);
}
