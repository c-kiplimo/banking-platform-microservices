package com.collicode.card.business.domain.service.impl;

import com.collicode.card.business.domain.Card;
import com.collicode.card.business.domain.CardId;
import com.collicode.card.business.domain.adapter.write.CardWriteAdapter;
import com.collicode.card.business.domain.command.CardCommand;
import com.collicode.card.business.domain.service.CardValidationService;
import com.collicode.card.business.domain.valueobject.CardType;
import com.collicode.shared.domain.service.SequenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.collicode.card.business.domain.constants.CardConstants.ENTITYNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    private CardValidationService cardValidationService;
    private CardWriteAdapter cardWriteAdapter;
    private SequenceRepository<CardId, Long, Long> sequenceRepository;
    private CardServiceImpl cardService;

    @BeforeEach
    void setup() {
        cardValidationService = mock(CardValidationService.class);
        cardWriteAdapter = mock(CardWriteAdapter.class);
        sequenceRepository = mock(SequenceRepository.class);

        cardService = new CardServiceImpl(cardValidationService, cardWriteAdapter, sequenceRepository);
    }

    @Test
    void createCard_shouldCreateWithInitialId_whenNoPreviousIdExists() {
        CardCommand command = CardCommand.builder()
                .accountId(1000L)
                .alias("My Card Alias")
                .cardId(0L)
                .cardType(CardType.PHYSICAL)
                .pan("1234567890123456")
                .cvv("123")
                .build();

        CardId initialId = CardId.initialId();

        // No previous ID found
        when(sequenceRepository.findPreviousId(ENTITYNAME)).thenReturn(Mono.empty());

        // insertId returns Mono.empty to avoid NPE
        when(sequenceRepository.insertId(eq(ENTITYNAME), eq(initialId.getCurrentId()), eq(initialId.getFactor())))
                .thenReturn(Mono.empty());

        // Simulate validations return empty Mono (no error)
        when(cardValidationService.validateCardCreation(anyLong(), anyString())).thenReturn(Mono.empty());
        when(cardValidationService.checkDuplicateCard(anyLong())).thenReturn(Mono.empty());

        // Simulate createCard returns the Card
        when(cardWriteAdapter.createCard(any(Card.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Card> result = cardService.createCard(command);

        StepVerifier.create(result)
                .assertNext(card -> {
                    assertEquals(initialId.getCurrentId(), card.getCardId().getCurrentId());
                    assertEquals(command.getAlias(), card.getAlias());
                    assertEquals(command.getAccountId(), card.getAccountId());
                })
                .verifyComplete();

        verify(sequenceRepository).insertId(ENTITYNAME, initialId.getCurrentId(), initialId.getFactor());
        verify(cardValidationService).validateCardCreation(command.getAccountId(), command.getCardType().name());
        verify(cardValidationService).checkDuplicateCard(initialId.getCurrentId());
        verify(cardWriteAdapter).createCard(any(Card.class));
    }

    @Test
    void updateCard_shouldUpdateCardAlias() {
        CardCommand command = CardCommand.builder()
                .cardId(10L)
                .alias("Updated Alias")
                .build();

        Card existingCard = Card.builder()
                .cardId(CardId.of(10L, 1L))
                .alias("Old Alias")
                .build();

        Card updatedCard = existingCard.withAlias(command.getAlias());

        when(cardValidationService.findAccountByCardId(command.getCardId()))
                .thenReturn(Mono.just(existingCard));

        when(cardWriteAdapter.updateCard(any(Card.class)))
                .thenReturn(Mono.just(updatedCard));

        StepVerifier.create(cardService.updateCard(command))
                .expectNextMatches(card -> card.getAlias().equals(command.getAlias()))
                .verifyComplete();

        verify(cardValidationService).findAccountByCardId(command.getCardId());
        verify(cardWriteAdapter).updateCard(any(Card.class));
    }

    @Test
    void deleteCard_shouldDeleteCardSuccessfully() {
        long cardId = 15L;
        Card mockCard = mock(Card.class);

        when(cardValidationService.findAccountByCardId(cardId))
                .thenReturn(Mono.just(mockCard));

        when(cardWriteAdapter.deleteCard(mockCard))
                .thenReturn(Mono.empty());

        StepVerifier.create(cardService.deleteCard(cardId))
                .verifyComplete();

        verify(cardValidationService).findAccountByCardId(cardId);
        verify(cardWriteAdapter).deleteCard(mockCard);
    }

    @Test
    void identity_shouldReturnInitialCardId_whenNoPreviousExists() {
        CardId initialId = CardId.initialId();

        when(sequenceRepository.findPreviousId(ENTITYNAME)).thenReturn(Mono.empty());
        when(sequenceRepository.insertId(eq(ENTITYNAME), eq(initialId.getCurrentId()), eq(initialId.getFactor())))
                .thenReturn(Mono.empty());

        StepVerifier.create(cardService.identity())
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
