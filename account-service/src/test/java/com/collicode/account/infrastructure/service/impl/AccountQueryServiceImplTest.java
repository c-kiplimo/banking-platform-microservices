package com.collicode.account.infrastructure.service.impl;


import com.collicode.account.infrastructure.repository.model.read.AccountReadModel;
import com.collicode.account.infrastructure.repository.read.AccountReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class AccountQueryServiceImplTest {

    private AccountReadRepository accountReadRepository;
    private AccountQueryServiceImpl accountQueryService;

    @BeforeEach
    void setUp() {
        accountReadRepository = Mockito.mock(AccountReadRepository.class);
        accountQueryService = new AccountQueryServiceImpl(accountReadRepository);
    }

    @Test
    void fetchAccount_ShouldReturnAccountReadModel() {
        AccountReadModel account = new AccountReadModel();
        account.setRecordId(1L);
        account.setIban("DE12345678901234567890");
        account.setBicSwift("DEUTDEFF");
        account.setCustomerId(100L);
        account.setCreatedAt(LocalDateTime.now().minusDays(5));
        account.setUpdatedAt(LocalDateTime.now());

        when(accountReadRepository.fetchAccountById(1L)).thenReturn(Mono.just(account));

        StepVerifier.create(accountQueryService.fetchAccount(1L))
                .expectNextMatches(acc -> acc.getRecordId() == 1L
                        && "DE12345678901234567890".equals(acc.getIban())
                        && acc.getCustomerId() == 100L)
                .verifyComplete();

        verify(accountReadRepository, times(1)).fetchAccountById(1L);
    }

    @Test
    void fetchAllAccounts_ShouldReturnFluxOfAccounts() {
        AccountReadModel account1 = new AccountReadModel();
        account1.setRecordId(1L);
        account1.setIban("DE12345678901234567890");
        account1.setBicSwift("DEUTDEFF");
        account1.setCustomerId(100L);
        account1.setCreatedAt(LocalDateTime.now().minusDays(5));
        account1.setUpdatedAt(LocalDateTime.now());

        AccountReadModel account2 = new AccountReadModel();
        account2.setRecordId(2L);
        account2.setIban("FR12345678901234567890");
        account2.setBicSwift("AGRIFRPP");
        account2.setCustomerId(101L);
        account2.setCreatedAt(LocalDateTime.now().minusDays(3));
        account2.setUpdatedAt(LocalDateTime.now());

        Map<String, String> filters = new HashMap<>();
        filters.put("customerId", "100");

        when(accountReadRepository.fetchAllAccounts(filters)).thenReturn(Flux.just(account1, account2));

        StepVerifier.create(accountQueryService.fetchAllAccounts(filters))
                .expectNextCount(2)
                .verifyComplete();

        verify(accountReadRepository, times(1)).fetchAllAccounts(filters);
    }
}
