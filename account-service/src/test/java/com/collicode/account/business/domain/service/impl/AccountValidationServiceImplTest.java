package com.collicode.account.business.domain.service.impl;

import com.collicode.account.business.domain.Account;
import com.collicode.account.business.domain.AccountId;
import com.collicode.account.business.domain.adapter.read.AccountReadAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountValidationServiceImplTest {

    private AccountReadAdapter accountReadAdapter;
    private AccountValidationServiceImpl accountValidationService;

    @BeforeEach
    void setUp() {
        accountReadAdapter = mock(AccountReadAdapter.class);
        accountValidationService = new AccountValidationServiceImpl(accountReadAdapter);
    }

    @Test
    void findAccountByAccountId_shouldReturnAccount_whenFound() {
        AccountId accountId = AccountId.of(123L, 1L);
        Account account = Account.builder().accountId(accountId).build();


        when(accountReadAdapter.findCustomerByAccountId(123L)).thenReturn(Mono.just(account));

        StepVerifier.create(accountValidationService.findAccountByAccountId(123L))
                .expectNext(account)
                .verifyComplete();
    }


    @Test
    void checkDuplicateAccount_shouldReturnEmpty_whenAccountDoesNotExist() {
        when(accountReadAdapter.findCustomerByAccountId(123L)).thenReturn(Mono.empty());

        StepVerifier.create(accountValidationService.checkDuplicateAccount(123L))
                .verifyComplete();
    }
}
