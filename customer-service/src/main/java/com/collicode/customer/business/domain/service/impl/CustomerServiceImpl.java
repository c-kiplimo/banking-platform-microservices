package com.collicode.customer.business.domain.service.impl;

import com.collicode.customer.business.domain.Customer;
import com.collicode.customer.business.domain.CustomerId;
import com.collicode.customer.business.domain.adapter.write.CustomerWriteAdapter;
import com.collicode.customer.business.domain.command.CustomerCommand;
import com.collicode.customer.business.domain.service.CustomerService;
import com.collicode.customer.business.domain.service.CustomerValidationService;
import com.collicode.shared.domain.service.SequenceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.collicode.customer.business.domain.constants.CustomerConstants.ENTITYNAME;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerValidationService customerValidationService;
    private final CustomerWriteAdapter customerWriteAdapter;
    private final SequenceRepository<CustomerId, Long, Long> sequenceRepository;

    public CustomerServiceImpl(CustomerValidationService customerValidationService, CustomerWriteAdapter customerWriteAdapter, SequenceRepository<CustomerId, Long, Long> sequenceRepository) {
        this.customerValidationService = customerValidationService;
        this.customerWriteAdapter = customerWriteAdapter;
        this.sequenceRepository = sequenceRepository;
    }

    public Mono<Customer> createCustomer(CustomerCommand customerCommand) {
        return identity()
                .flatMap(
                        customerId -> createCustomer(Customer.from(customerCommand, customerId))
                );
    }

    @Override
    public Mono<Customer> updateCustomer(CustomerCommand customerCommand) {
        return customerValidationService.findCustomerByCustomerId(customerCommand.getCustomerId()) // fetch existing customer
                .map(existingCustomer -> existingCustomer.withNames(customerCommand.getFirstName(), customerCommand.getLastName(), customerCommand.getOtherName()))
                .flatMap(customerWriteAdapter::updateCustomer);
    }


    @Override
    public Mono<Customer> deleteCustomer(long customerId) {
        return customerValidationService.findCustomerByCustomerId(customerId)
                .flatMap(customerWriteAdapter::deleteCustomer);
    }


    public Mono<Customer> createCustomer(Customer customer) {
        return customerValidationService.checkDuplicateCustomer(
                        customer.getCustomerId().getCurrentId())
                .switchIfEmpty(customerWriteAdapter.createCustomer(customer))
                .cast(Customer.class);
    }

    public Mono<CustomerId> identity() {
        return sequenceRepository.findPreviousId(ENTITYNAME)
                .map((identity) -> CustomerId.of(identity.getCurrentId(),
                        identity.getFactor()).nextId())
                .flatMap(customerId -> sequenceRepository.updateId(ENTITYNAME,
                        customerId.getCurrentId()).thenReturn(customerId))
                .switchIfEmpty(sequenceRepository.insertId(ENTITYNAME,
                                CustomerId.initialId().getCurrentId(),
                                CustomerId.initialId().getFactor())
                        .thenReturn(CustomerId.initialId()));

    }
}
