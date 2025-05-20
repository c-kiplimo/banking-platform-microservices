package com.collicode.account.business.domain;

import com.collicode.account.business.domain.command.AccountCommand;
import com.collicode.shared.exception.BusinessEntityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void shouldCreateAccountSuccessfullyUsingBuilder() {
        AccountId accountId = AccountId.of(1_000_001L);
        String iban = "DE89370400440532013000";
        String bicSwift = "COBADEFFXXX";
        long customerId = 123L;

        Account account = Account.builder()
                .accountId(accountId)
                .iban(iban)
                .bicSwift(bicSwift)
                .customerId(customerId)
                .build();

        assertEquals(accountId, account.getAccountId());
        assertEquals(iban, account.getIban());
        assertEquals(bicSwift, account.getBicSwift());
        assertEquals(customerId, account.getCustomerId());
    }

    @Test
    void shouldCreateAccountUsingFromCommand() {
        AccountCommand command = AccountCommand.builder()
                .iban("DE44500105175407324931")
                .bicSwift("INGDDEFFXXX")
                .customerId(456L)
                .build();

        AccountId accountId = AccountId.of(1_000_005L);

        Account account = Account.from(command, accountId);

        assertEquals(accountId, account.getAccountId());
        assertEquals(command.getIban(), account.getIban());
        assertEquals(command.getBicSwift(), account.getBicSwift());
        assertEquals(command.getCustomerId(), account.getCustomerId());
    }

    @Test
    void shouldThrowExceptionWhenAccountIdIsMissing() {
        Account account = Account.builder()
                .iban("DE44500105175407324931")
                .bicSwift("INGDDEFFXXX")
                .customerId(123L)
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                account::validate
        );
        assertTrue(exception.getMessage().contains("AccountId"));
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsZero() {
        Account account = Account.builder()
                .accountId(AccountId.of(1_000_002L))
                .iban("DE44500105175407324931")
                .bicSwift("INGDDEFFXXX")
                .customerId(0L)
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                account::validate
        );
        assertTrue(exception.getMessage().contains("CustomerId"));
    }

    @Test
    void shouldThrowExceptionWhenIbanIsMissing() {
        Account account = Account.builder()
                .accountId(AccountId.of(1_000_003L))
                .bicSwift("INGDDEFFXXX")
                .customerId(100L)
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                account::validate
        );
        assertTrue(exception.getMessage().contains("IBAN"));
    }

    @Test
    void shouldThrowExceptionWhenBicSwiftIsMissing() {
        Account account = Account.builder()
                .accountId(AccountId.of(1_000_004L))
                .iban("DE44500105175407324931")
                .customerId(100L)
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                account::validate
        );
        assertTrue(exception.getMessage().contains("BIC/SWIFT"));
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        Account account = Account.builder()
                .accountId(AccountId.of(1_000_005L))
                .iban("DE44500105175407324931")
                .bicSwift("INGDDEFFXXX")
                .customerId(100L)
                .build();

        assertDoesNotThrow(account::validate);
    }
}
