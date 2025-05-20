package com.collicode.customer.business.application.commandservice;

import com.collicode.customer.business.domain.command.CustomerCommand;
import com.collicode.customer.business.domain.service.CustomerService;
import com.collicode.customer.infrastructure.dto.request.CustomerRequest;
import com.collicode.customer.infrastructure.dto.response.CustomerResponse;
import com.collicode.shared.domain.api.CommandResult;
import com.collicode.shared.dto.Command;
import com.collicode.shared.service.BusinessCommandHandler;
import com.collicode.shared.service.CommandType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@CommandType(entityName = Constants.CUSTOMER, action = Constants.UPDATE)
public class UpdateCustomerCommandHandler implements BusinessCommandHandler<CustomerRequest, CustomerResponse> {
    private final CustomerService customerService;

    public UpdateCustomerCommandHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Function<Command<CustomerRequest>, Mono<CommandResult<CustomerResponse>>> processCommand() {
        return command -> {
            CustomerRequest request = command.getPayload();
            CustomerCommand updateCustomerCommand = toUpdateCustomerCommand(request);

            return customerService.updateCustomer(updateCustomerCommand)
                    .map(customer -> CommandResult.<CustomerResponse>builder()
                            .commandId(command.getEntityName() + "_" + command.getAction())
                            .entityName(command.getEntityName())
                            .actionName(command.getAction())
                            .transactionId(command.getTrace().getTransactionId())
                            .result(toCustomerResponse(request).withCustomerId(customer.getCustomerId().getCurrentId()))
                            .trace(command.getTrace())
                            .build());
        };
    }

    private CustomerCommand toUpdateCustomerCommand(CustomerRequest request) {
        return CustomerCommand.builder()
                .customerId(request.getCustomerId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .otherName(request.getOtherName())
                .build();
    }

    private CustomerResponse toCustomerResponse(CustomerRequest request) {
        return CustomerResponse.builder()
                .customerId(request.getCustomerId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .otherName(request.getOtherName())
                .build();
    }
}
