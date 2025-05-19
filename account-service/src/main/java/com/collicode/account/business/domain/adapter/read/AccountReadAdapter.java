package com.collicode.account.business.domain.adapter.read;

import com.collicode.account.business.domain.Account;
import reactor.core.publisher.Mono;

public interface AccountReadAdapter {
    Mono<Account> findCustomerByAccountId(long accountId);
}
