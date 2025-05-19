package com.collicode.account.business.domain.service;

import com.collicode.account.business.domain.Account;
import reactor.core.publisher.Mono;

public interface AccountValidationService {
    Mono<Account> findAccountByAccountId(long accountId);

    Mono<Account> checkDuplicateAccount(long accountId);

}
