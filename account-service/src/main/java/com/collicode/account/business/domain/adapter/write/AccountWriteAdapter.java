package com.collicode.account.business.domain.adapter.write;

import com.collicode.account.business.domain.Account;
import reactor.core.publisher.Mono;

public interface AccountWriteAdapter {
    Mono<Account> createAccount(Account account);

    Mono<Void> deleteAccount(Account account);
}
