package com.collicode.account.business.domain.service.impl;

import com.collicode.account.business.domain.Account;
import com.collicode.account.business.domain.adapter.read.AccountReadAdapter;
import com.collicode.account.business.domain.service.AccountValidationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.collicode.account.business.domain.constants.AccountConstants.ENTITYNAME;
import static com.collicode.shared.exception.BusinessEntityException.entityNotFoundException;
import static com.collicode.shared.exception.BusinessException.duplicateKeyException;

@Service
public class AccountValidationServiceImpl implements AccountValidationService {
    private final AccountReadAdapter accountReadAdapter;

    public AccountValidationServiceImpl(AccountReadAdapter accountReadAdapter) {
        this.accountReadAdapter = accountReadAdapter;
    }

    @Override
    public Mono<Account> findAccountByAccountId(long accountId) {
        return accountReadAdapter.findCustomerByAccountId(accountId)
                .switchIfEmpty(Mono.error(entityNotFoundException(ENTITYNAME)));
    }

    @Override
    public Mono<Account> checkDuplicateAccount(long accountId) {
        return accountReadAdapter.findCustomerByAccountId(accountId)
                .flatMap(account -> Mono.error(duplicateKeyException(ENTITYNAME)));
    }
}
