package com.collicode.account.business.application.commandservice;


import com.collicode.account.business.domain.service.AccountService;
import com.collicode.account.infrastructure.dto.request.AccountRequest;
import com.collicode.shared.domain.api.CommandResult;
import com.collicode.shared.dto.Command;
import com.collicode.shared.service.BusinessCommandHandler;
import com.collicode.shared.service.CommandType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@CommandType(entityName = Constants.ACCOUNT, action = Constants.DELETE)
public class DeleteAccountCommandHandler implements BusinessCommandHandler<AccountRequest, AccountRequest> {

    private final AccountService accountService;

    public DeleteAccountCommandHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Function<Command<AccountRequest>, Mono<CommandResult<AccountRequest>>> processCommand() {
        return command -> {
            long accountId = command.getPayload().getAccountId();

            return accountService.deleteAccount(accountId)
                    .map(ignored -> CommandResult.<AccountRequest>builder()
                            .commandId(command.getEntityName() + "_" + command.getAction())
                            .entityName(command.getEntityName())
                            .actionName(command.getAction())
                            .transactionId(command.getTrace().getTransactionId())
                            .result(AccountRequest.builder().accountId(accountId).build())
                            .trace(command.getTrace())
                            .build());
        };
    }
}
