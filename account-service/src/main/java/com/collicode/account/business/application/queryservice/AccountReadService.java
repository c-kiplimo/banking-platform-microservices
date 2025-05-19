package com.collicode.account.business.application.queryservice;

import com.collicode.account.business.domain.Account;
import com.collicode.account.business.domain.AccountId;
import com.collicode.account.business.domain.adapter.read.AccountReadAdapter;
import com.collicode.account.infrastructure.repository.read.AccountReadRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountReadService implements AccountReadAdapter {
    private final AccountReadRepository accountReadRepository;

    public AccountReadService(AccountReadRepository accountReadRepository) {
        this.accountReadRepository = accountReadRepository;
    }


    @Override
    public Mono<Account> findCustomerByAccountId(long accountId) {
        return accountReadRepository.fetchAccountById(accountId)
                .map(accountReadModel -> Account.builder()
                        .accountId(AccountId.of(accountReadModel.getRecordId()))
                        .bicSwift(accountReadModel.getBicSwift())
                        .iban(accountReadModel.getIban())
                        .customerId(accountReadModel.getCustomerId())
                        .build());
    }
}
