package com.collicode.account.infrastructure.repository.read;


import com.collicode.account.infrastructure.repository.model.read.AccountReadModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface AccountReadRepository {
    Mono<AccountReadModel> fetchAccountById(long accountId);

    Flux<AccountReadModel> fetchAllAccounts(Map<String, String> filters);
}
