package com.collicode.account.business.application.outwardservice;

import com.collicode.account.business.domain.Account;
import com.collicode.account.business.domain.adapter.write.AccountWriteAdapter;
import com.collicode.account.infrastructure.repository.model.write.AccountWriteModel;
import com.collicode.account.infrastructure.repository.write.AccountWriteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountOutwardService implements AccountWriteAdapter {
    private final AccountWriteRepository accountWriteRepository;

    public AccountOutwardService(AccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    public Mono<Account> createAccount(Account account) {
        AccountWriteModel accountWriteModel = AccountWriteModel
                .builder()
                .recordId(account.getAccountId().getCurrentId())
                .bicSwift(account.getBicSwift())
                .iban(account.getIban())
                .customerId(account.getCustomerId())
                .build();
        return accountWriteRepository.createAccount(accountWriteModel)
                .thenReturn(account);
    }

    @Override
    public Mono<Void> deleteAccount(Account account) {

        AccountWriteModel accountWriteModel = AccountWriteModel
                .builder()
                .recordId(account.getAccountId().getCurrentId())
                .bicSwift(account.getBicSwift())
                .iban(account.getIban())
                .customerId(account.getCustomerId())
                .build();
        return accountWriteRepository.deleteAccount(accountWriteModel);
    }
}
