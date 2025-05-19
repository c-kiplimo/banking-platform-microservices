package com.collicode.account.business.domain.service;


import com.collicode.account.business.domain.Account;
import com.collicode.account.business.domain.command.AccountCommand;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Account> createAccount(AccountCommand accountCommand);

    Mono<Void> deleteAccount(long accountId);
}
