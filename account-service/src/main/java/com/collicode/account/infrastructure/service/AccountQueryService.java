package com.collicode.account.infrastructure.service;

import com.collicode.account.infrastructure.repository.model.read.AccountReadModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface AccountQueryService {
    Mono<AccountReadModel> fetchAccount(long accountId);

    Flux<AccountReadModel> fetchAllAccounts(Map<String, String> filters);
}
