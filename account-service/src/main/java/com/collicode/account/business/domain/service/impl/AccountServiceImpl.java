package com.collicode.account.business.domain.service.impl;

import com.collicode.account.business.domain.Account;
import com.collicode.account.business.domain.AccountId;
import com.collicode.account.business.domain.adapter.write.AccountWriteAdapter;
import com.collicode.account.business.domain.command.AccountCommand;
import com.collicode.account.business.domain.service.AccountService;
import com.collicode.account.business.domain.service.AccountValidationService;
import com.collicode.shared.domain.service.SequenceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.collicode.account.business.domain.constants.AccountConstants.ENTITYNAME;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountValidationService accountValidationService;
    private final AccountWriteAdapter accountWriteAdapter;
    private final SequenceRepository<AccountId, Long, Long> sequenceRepository;

    public AccountServiceImpl(AccountValidationService accountValidationService,
                              AccountWriteAdapter accountWriteAdapter,
                              SequenceRepository<AccountId, Long, Long> sequenceRepository) {
        this.accountValidationService = accountValidationService;
        this.accountWriteAdapter = accountWriteAdapter;
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public Mono<Account> createAccount(AccountCommand accountCommand) {
        return identity()
                .flatMap(accountId -> createAccount(Account.from(accountCommand, accountId)));
    }

    @Override
    public Mono<Void> deleteAccount(long accountId) {
        return accountValidationService.findAccountByAccountId(accountId)
                .flatMap(accountWriteAdapter::deleteAccount);
    }

    public Mono<Account> createAccount(Account account) {
        return accountValidationService.checkDuplicateAccount(account.getAccountId().getCurrentId())
                .switchIfEmpty(accountWriteAdapter.createAccount(account))
                .cast(Account.class);
    }

    public Mono<AccountId> identity() {
        return sequenceRepository.findPreviousId(ENTITYNAME)
                .map(identity -> AccountId.of(identity.getCurrentId(), identity.getFactor()).nextId())
                .flatMap(accountId -> sequenceRepository.updateId(ENTITYNAME, accountId.getCurrentId())
                        .thenReturn(accountId))
                .switchIfEmpty(sequenceRepository.insertId(
                                ENTITYNAME,
                                AccountId.initialId().getCurrentId(),
                                AccountId.initialId().getFactor()
                        )
                        .thenReturn(AccountId.initialId()));
    }

}
