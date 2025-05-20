package com.collicode.customer.business.application.commandservice;

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
@CommandType(entityName = Constants.CUSTOMER, action = Constants.DELETE)
public class DeleteCustomerCommandHandler implements BusinessCommandHandler<CustomerRequest, CustomerResponse> {

    private final CustomerService customerService;

    public DeleteCustomerCommandHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Function<Command<CustomerRequest>, Mono<CommandResult<CustomerResponse>>> processCommand() {
        return command -> {
            Long customerId = command.getPayload().getCustomerId();

            return customerService.deleteCustomer(customerId)
                    .map(deletedCustomer -> CommandResult.<CustomerResponse>builder()
                            .commandId(command.getEntityName() + "_" + command.getAction())
                            .entityName(command.getEntityName())
                            .actionName(command.getAction())
                            .transactionId(command.getTrace().getTransactionId())
                            .result(CustomerResponse.builder()
                                    .customerId(customerId)
                                    .build())
                            .trace(command.getTrace())
                            .build());
        };
    }
}
