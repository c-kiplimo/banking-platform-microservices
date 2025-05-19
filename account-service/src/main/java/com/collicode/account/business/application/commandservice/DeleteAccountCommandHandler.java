package com.collicode.account.business.application.commandservice;

import com.collicode.account.business.domain.command.AccountCommand;
import com.collicode.account.business.domain.service.AccountService;
import com.collicode.account.infrastructure.client.CustomerClient;
import com.collicode.account.infrastructure.dto.request.AccountRequest;
import com.collicode.account.infrastructure.dto.response.AccountResponse;
import com.collicode.shared.domain.api.CommandResult;
import com.collicode.shared.dto.Command;
import com.collicode.shared.service.BusinessCommandHandler;
import com.collicode.shared.service.CommandType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.collicode.shared.exception.BusinessEntityException.entityNotFoundException;

@Service
@CommandType(entityName = Constants.ACCOUNT, action = Constants.DELETE)
public class DeleteAccountCommandHandler implements BusinessCommandHandler<AccountRequest, AccountResponse> {

    private final AccountService accountService;
    private final CustomerClient customerClient;  // inject CustomerClient

    public DeleteAccountCommandHandler(AccountService accountService, CustomerClient customerClient) {
        this.accountService = accountService;
        this.customerClient = customerClient;
    }

    @Override
    public Function<Command<AccountRequest>, Mono<CommandResult<AccountResponse>>> processCommand() {
        return command -> {
            AccountRequest request = command.getPayload();

            // Validate customer existence reactively via CustomerClient
            return customerClient.getcustomerById(request.getCustomerId())
                    .switchIfEmpty(Mono.error(entityNotFoundException(
                            "Invalid Customer ID: " + request.getCustomerId())))
                    .flatMap(customerResponse -> {
                        AccountCommand createAccountCommand = toCreateAccountCommand(request);
                        return accountService.createAccount(createAccountCommand)
                                .map(account -> CommandResult.<AccountResponse>builder()
                                        .commandId(command.getEntityName() + "_" + command.getAction())
                                        .entityName(command.getEntityName())
                                        .actionName(command.getAction())
                                        .transactionId(command.getTrace().getTransactionId())
                                        .result(toAccountResponse(request).withAccountId(account.getAccountId().getCurrentId()))
                                        .trace(command.getTrace())
                                        .build());
                    });
        };
    }

    private AccountCommand toCreateAccountCommand(AccountRequest request) {
        return AccountCommand.builder()
                .bicSwift(request.getBicSwift())
                .iban(request.getIban())
                .customerId(request.getCustomerId())
                .build();
    }

    private AccountResponse toAccountResponse(AccountRequest request) {
        return AccountResponse.builder()
                .accountId(request.getAccountId())
                .bicSwift(request.getBicSwift())
                .iban(request.getIban())
                .customerId(request.getCustomerId())
                .build();
    }
}
