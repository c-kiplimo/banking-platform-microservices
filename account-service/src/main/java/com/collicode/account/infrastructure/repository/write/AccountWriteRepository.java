package com.collicode.account.infrastructure.repository.write;

import com.collicode.account.infrastructure.repository.model.write.AccountWriteModel;
import reactor.core.publisher.Mono;

public interface AccountWriteRepository {
    Mono<AccountWriteModel> createAccount(AccountWriteModel accountWriteModel);

    Mono<Void> deleteAccount(AccountWriteModel accountWriteModel);
}
