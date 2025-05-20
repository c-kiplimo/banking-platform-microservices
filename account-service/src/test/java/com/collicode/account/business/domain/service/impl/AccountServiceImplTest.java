package com.collicode.account.business.domain.service.impl;

import com.collicode.account.business.domain.Account;
import com.collicode.account.business.domain.AccountId;
import com.collicode.account.business.domain.adapter.write.AccountWriteAdapter;
import com.collicode.account.business.domain.command.AccountCommand;
import com.collicode.account.business.domain.service.AccountValidationService;
import com.collicode.shared.domain.service.SequenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private static final String ENTITYNAME = "ACCOUNT";
    private AccountValidationService accountValidationService;
    private AccountWriteAdapter accountWriteAdapter;
    private SequenceRepository<AccountId, Long, Long> sequenceRepository;
    private AccountServiceImpl accountService;

    @BeforeEach
    void setup() {
        accountValidationService = mock(AccountValidationService.class);
        accountWriteAdapter = mock(AccountWriteAdapter.class);
        sequenceRepository = mock(SequenceRepository.class);

        accountService = new AccountServiceImpl(accountValidationService, accountWriteAdapter, sequenceRepository);
    }

    @Test
    void createAccount_shouldCreateWithInitialId_whenNoPreviousIdExists() {
        AccountCommand command = AccountCommand.builder()
                .iban("DE12345678901234567890")
                .bicSwift("SBICKENX")
                .customerId(300L)
                .build();

        AccountId initialId = AccountId.initialId();

        // Simulate no existing ID
        when(sequenceRepository.findPreviousId(anyString()))
                .thenReturn(Mono.empty());

        // Provide stubbed insertId to avoid NPE
        when(sequenceRepository.insertId(anyString(), anyLong(), anyLong()))
                .thenReturn(Mono.empty());

        when(accountValidationService.checkDuplicateAccount(anyLong()))
                .thenReturn(Mono.empty());

        when(accountWriteAdapter.createAccount(any(Account.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Account> result = accountService.createAccount(command);

        StepVerifier.create(result)
                .assertNext(account -> {
                    assertEquals(initialId.getCurrentId(), account.getAccountId().getCurrentId());
                    assertEquals(command.getIban(), account.getIban());
                })
                .verifyComplete();

        verify(sequenceRepository).insertId(anyString(), anyLong(), anyLong());
    }


    @Test
    void deleteAccount_shouldDeleteAccountSuccessfully() {
        long accountId = 12345L;
        Account mockAccount = mock(Account.class);

        when(accountValidationService.findAccountByAccountId(accountId))
                .thenReturn(Mono.just(mockAccount));

        when(accountWriteAdapter.deleteAccount(mockAccount))
                .thenReturn(Mono.empty());

        Mono<Void> result = accountService.deleteAccount(accountId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(accountValidationService).findAccountByAccountId(accountId);
        verify(accountWriteAdapter).deleteAccount(mockAccount);
    }


    @Test
    void identity_shouldReturnInitialAccountId_whenNoPreviousExists() {
        AccountId initialId = AccountId.initialId();

        when(sequenceRepository.findPreviousId(ENTITYNAME)).thenReturn(Mono.empty());
        when(sequenceRepository.insertId(eq(ENTITYNAME), eq(initialId.getCurrentId()), eq(initialId.getFactor())))
                .thenReturn(Mono.empty());

        StepVerifier.create(accountService.identity())
                .assertNext(id -> {
                    assertEquals(initialId.getCurrentId(), id.getCurrentId());
                    assertEquals(initialId.getFactor(), id.getFactor());
                })
                .verifyComplete();

        verify(sequenceRepository).findPreviousId(ENTITYNAME);
        verify(sequenceRepository).insertId(ENTITYNAME, initialId.getCurrentId(), initialId.getFactor());
        verify(sequenceRepository, never()).updateId(anyString(), anyLong());
    }
}
