package com.collicode.account.business.domain;

import com.collicode.account.business.domain.command.AccountCommand;
import com.collicode.shared.domain.BusinessEntity;
import lombok.Getter;

import java.util.Objects;

import static com.collicode.account.business.domain.constants.AccountConstants.ENTITYNAME;
import static com.collicode.shared.exception.BusinessEntityException.missingKeyException;
import static com.collicode.shared.util.BusinessValidation.ifHasNoText;
import static com.collicode.shared.util.BusinessValidation.ifTrue;

@Getter
public class Account extends BusinessEntity {

    private final AccountId accountId;
    private final String iban;
    private final String bicSwift;
    private final long customerId;

    private Account(AccountBuilder builder) {
        this.accountId = builder.accountId;
        this.iban = builder.iban;
        this.bicSwift = builder.bicSwift;
        this.customerId = builder.customerId;
    }

    public static Account from(AccountCommand accountCommand, AccountId accountId) {
        return Account.builder()
                .accountId(accountId)
                .iban(accountCommand.getIban())
                .bicSwift(accountCommand.getBicSwift())
                .customerId(accountCommand.getCustomerId())
                .build();
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    @Override
    public void validate() {
        ifTrue(Objects.isNull(this.accountId), missingKeyException(ENTITYNAME, "AccountId"));
        ifTrue(this.customerId == 0, missingKeyException(ENTITYNAME, "CustomerId"));
        ifHasNoText(this.iban, missingKeyException(ENTITYNAME, "IBAN"));
        ifHasNoText(this.bicSwift, missingKeyException(ENTITYNAME, "BIC/SWIFT"));
    }

    public static class AccountBuilder {
        private AccountId accountId;
        private String iban;
        private String bicSwift;
        private long customerId;

        public AccountBuilder accountId(AccountId accountId) {
            this.accountId = accountId;
            return this;
        }

        public AccountBuilder iban(String iban) {
            this.iban = iban;
            return this;
        }

        public AccountBuilder bicSwift(String bicSwift) {
            this.bicSwift = bicSwift;
            return this;
        }

        public AccountBuilder customerId(long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}
