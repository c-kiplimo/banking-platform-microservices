package com.collicode.account.infrastructure.repository.write.impl;

import com.collicode.account.infrastructure.repository.model.write.AccountWriteModel;
import com.collicode.account.infrastructure.repository.write.AccountWriteRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class AccountWriteRepositoryImpl implements AccountWriteRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public AccountWriteRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<AccountWriteModel> createAccount(AccountWriteModel accountWriteModel) {
        return r2dbcEntityTemplate.insert(accountWriteModel);
    }

    @Override
    public Mono<Void> deleteAccount(AccountWriteModel accountWriteModel) {
        return r2dbcEntityTemplate.delete(accountWriteModel)
                .then();
    }
}
