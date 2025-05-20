package com.collicode.card.business.domain;

import com.collicode.card.business.domain.command.CardCommand;
import com.collicode.card.business.domain.valueobject.CardType;
import com.collicode.shared.exception.BusinessEntityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void shouldCreateCardSuccessfullyUsingBuilder() {
        CardId cardId = CardId.of(1_000_001L);
        Card card = Card.builder()
                .cardId(cardId)
                .alias("Personal Visa")
                .accountId(123L)
                .cardType(CardType.VIRTUAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        assertEquals(cardId, card.getCardId());
        assertEquals("Personal Visa", card.getAlias());
        assertEquals(123L, card.getAccountId());
        assertEquals(CardType.VIRTUAL, card.getCardType());
        assertEquals("4111111111111111", card.getPan());
        assertEquals("123", card.getCvv());
    }

    @Test
    void shouldCreateCardUsingFromCommand() {
        CardCommand command = CardCommand.builder()
                .alias("Work Mastercard")
                .accountId(456L)
                .cardType(CardType.PHYSICAL)
                .pan("5500000000000004")
                .cvv("456")
                .build();

        CardId cardId = CardId.of(1_000_002L);

        Card card = Card.from(command, cardId);

        assertEquals(cardId, card.getCardId());
        assertEquals("Work Mastercard", card.getAlias());
        assertEquals(456L, card.getAccountId());
        assertEquals(CardType.PHYSICAL, card.getCardType());
        assertEquals("5500000000000004", card.getPan());
        assertEquals("456", card.getCvv());
    }

    @Test
    void shouldThrowExceptionWhenCardIdIsNull() {
        Card card = Card.builder()
                .alias("Invalid")
                .accountId(123L)
                .cardType(CardType.VIRTUAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                card::validate
        );
        assertTrue(exception.getMessage().contains("CardId"));
    }

    @Test
    void shouldThrowExceptionWhenAccountIdIsZero() {
        Card card = Card.builder()
                .cardId(CardId.of(1_000_003L))
                .alias("Invalid")
                .accountId(0L)
                .cardType(CardType.PHYSICAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                card::validate
        );
        assertTrue(exception.getMessage().contains("AccountId"));
    }

    @Test
    void shouldThrowExceptionWhenCardTypeIsNull() {
        Card card = Card.builder()
                .cardId(CardId.of(1_000_004L))
                .alias("Invalid")
                .accountId(123L)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                card::validate
        );
        assertTrue(exception.getMessage().contains("CardType"));
    }

    @Test
    void shouldThrowExceptionWhenPanIsEmpty() {
        Card card = Card.builder()
                .cardId(CardId.of(1_000_005L))
                .alias("Invalid")
                .accountId(123L)
                .cardType(CardType.VIRTUAL)
                .pan("")
                .cvv("123")
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                card::validate
        );
        assertTrue(exception.getMessage().contains("PAN"));
    }

    @Test
    void shouldThrowExceptionWhenCvvIsEmpty() {
        Card card = Card.builder()
                .cardId(CardId.of(1_000_006L))
                .alias("Invalid")
                .accountId(123L)
                .cardType(CardType.VIRTUAL)
                .pan("4111111111111111")
                .cvv("")
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                card::validate
        );
        assertTrue(exception.getMessage().contains("CVV"));
    }

    @Test
    void shouldThrowExceptionWhenCvvLengthIsInvalid() {
        Card card = Card.builder()
                .cardId(CardId.of(1_000_007L))
                .alias("Invalid")
                .accountId(123L)
                .cardType(CardType.VIRTUAL)
                .pan("4111111111111111")
                .cvv("12")
                .build();

        BusinessEntityException exception = assertThrows(
                BusinessEntityException.class,
                card::validate
        );
        assertTrue(exception.getMessage().contains("CVV"));
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        Card card = Card.builder()
                .cardId(CardId.of(1_000_008L))
                .alias("Valid Card")
                .accountId(123L)
                .cardType(CardType.PHYSICAL)
                .pan("4111111111111111")
                .cvv("999")
                .build();

        assertDoesNotThrow(card::validate);
    }

    @Test
    void shouldUpdateAliasUsingWithAliasMethod() {
        Card original = Card.builder()
                .cardId(CardId.of(1_000_009L))
                .alias("Old Alias")
                .accountId(123L)
                .cardType(CardType.PHYSICAL)
                .pan("5500000000000004")
                .cvv("321")
                .build();

        Card updated = original.withAlias("New Alias");

        assertEquals("New Alias", updated.getAlias());
        assertEquals(original.getCardId(), updated.getCardId());
        assertEquals(original.getAccountId(), updated.getAccountId());
        assertEquals(original.getCardType(), updated.getCardType());
        assertEquals(original.getPan(), updated.getPan());
        assertEquals(original.getCvv(), updated.getCvv());
    }

    @Test
    void shouldCreateVirtualCardSuccessfully() {
        Card card = Card.builder()
                .cardId(CardId.of(1_000_010L))
                .alias("Virtual Card")
                .accountId(999L)
                .cardType(CardType.VIRTUAL)
                .pan("4000000000000002")
                .cvv("111")
                .build();

        assertEquals(CardType.VIRTUAL, card.getCardType());
        assertDoesNotThrow(card::validate);
    }

    @Test
    void shouldCreatePhysicalCardSuccessfully() {
        Card card = Card.builder()
                .cardId(CardId.of(1_000_011L))
                .alias("Physical Card")
                .accountId(888L)
                .cardType(CardType.PHYSICAL)
                .pan("4200000000000003")
                .cvv("222")
                .build();

        assertEquals(CardType.PHYSICAL, card.getCardType());
        assertDoesNotThrow(card::validate);
    }
}
