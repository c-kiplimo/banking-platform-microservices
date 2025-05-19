package com.collicode.account.infrastructure.service.impl;

import com.collicode.account.infrastructure.repository.model.read.AccountReadModel;
import com.collicode.account.infrastructure.repository.read.AccountReadRepository;
import com.collicode.account.infrastructure.service.AccountQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AccountQueryServiceImpl implements AccountQueryService {
    private final AccountReadRepository accountReadRepository;

    public AccountQueryServiceImpl(AccountReadRepository accountReadRepository) {
        this.accountReadRepository = accountReadRepository;
    }


    @Override
    public Mono<AccountReadModel> fetchAccount(long accountId) {
        return accountReadRepository.fetchAccountById(accountId);
    }

    @Override
    public Flux<AccountReadModel> fetchAllAccounts(Map<String, String> filters) {
        return accountReadRepository.fetchAllAccounts(filters);
    }


}
