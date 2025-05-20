package com.collicode.customer.business.application.outwardservice;

import com.collicode.customer.business.domain.Customer;
import com.collicode.customer.business.domain.adapter.write.CustomerWriteAdapter;
import com.collicode.customer.infrastructure.repository.model.write.CustomerWriteModel;
import com.collicode.customer.infrastructure.repository.write.CustomerWriteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerOutwardService implements CustomerWriteAdapter {
    private final CustomerWriteRepository customerWriteRepository;

    public CustomerOutwardService(CustomerWriteRepository customerWriteRepository) {
        this.customerWriteRepository = customerWriteRepository;
    }

    private static CustomerWriteModel getCustomerWriteModel(Customer customer) {
        CustomerWriteModel customerWriteModel = CustomerWriteModel
                .builder()
                .recordId(customer.getCustomerId().getCurrentId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .otherName(customer.getOtherName())
                .build();
        return customerWriteModel;
    }

    @Override
    public Mono<Customer> createCustomer(Customer customer) {
        CustomerWriteModel customerWriteModel = getCustomerWriteModel(customer);
        return customerWriteRepository.createCustomer(customerWriteModel)
                .thenReturn(customer);
    }

    @Override
    public Mono<Customer> updateCustomer(Customer customer) {
        CustomerWriteModel customerWriteModel = getCustomerWriteModel(customer);
        return customerWriteRepository.updateCustomer(customerWriteModel)
                .thenReturn(customer);
    }

    @Override
    public Mono<Customer> deleteCustomer(Customer customer) {
        CustomerWriteModel customerWriteModel = getCustomerWriteModel(customer);
        return customerWriteRepository.deleteCustomer(customerWriteModel)
                .thenReturn(customer);
    }
}
