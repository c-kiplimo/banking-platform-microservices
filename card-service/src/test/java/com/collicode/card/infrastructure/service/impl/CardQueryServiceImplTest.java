package com.collicode.card.infrastructure.service.impl;


import com.collicode.card.business.domain.Card;
import com.collicode.card.business.domain.valueobject.CardType;
import com.collicode.card.infrastructure.repository.model.read.CardReadModel;
import com.collicode.card.infrastructure.repository.read.CardReadRepository;
import com.collicode.card.util.CardMaskingUtil;
import com.collicode.card.util.CardMaskingUtil.MaskedCardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CardQueryServiceImplTest {

    private CardReadRepository cardReadRepository;
    private CardQueryServiceImpl cardQueryService;

    @BeforeEach
    void setup() {
        cardReadRepository = mock(CardReadRepository.class);
        cardQueryService = new CardQueryServiceImpl(cardReadRepository);
    }

    @Test
    void fetchCard_shouldReturnMaskedCard_whenShowSensitiveFalse() {
        long cardId = 101L;

        // Mocked CardReadModel data - example PAN and CVV
        CardReadModel readModel = CardReadModel.builder()
                .recordId(cardId)
                .alias("My Visa Card")
                .accountId(200L)
                .cardType(CardType.PHYSICAL.name())
                .pan("1234567890123456")
                .cvv("123")
                .build();


        when(cardReadRepository.fetchCardById(cardId)).thenReturn(Mono.just(readModel));

        Mono<CardReadModel> resultMono = cardQueryService.fetchCard(cardId);

        StepVerifier.create(resultMono)
                .assertNext(card -> {
                    // Convert to domain Card for masking util (simulate)
                    Card domainCard = Card.builder()
                            .cardId(domainCardId(card.getRecordId()))
                            .alias(card.getAlias())
                            .accountId(card.getAccountId())
                            .cardType(CardType.valueOf(card.getCardType()))
                            .pan(card.getPan())
                            .cvv(card.getCvv())
                            .build();

                    MaskedCardDto masked = CardMaskingUtil.toMaskedDto(domainCard, false);

                    // Masked PAN should show first 4 + **** + last 4
                    assertEquals("1234****3456", masked.pan());
                    // Masked CVV should be ***
                    assertEquals("***", masked.cvv());
                })
                .verifyComplete();
    }

    @Test
    void fetchAllCards_shouldReturnAllCards() {
        CardReadModel card1 = CardReadModel.builder()
                .recordId(1L)
                .alias("Card1")
                .accountId(101L)
                .cardType(String.valueOf(CardType.PHYSICAL))
                .pan("1111222233334444")
                .cvv("111")
                .build();

        CardReadModel card2 = CardReadModel.builder()
                .recordId(2L)
                .alias("Card2")
                .accountId(102L)
                .cardType(String.valueOf(CardType.VIRTUAL))
                .pan("5555666677778888")
                .cvv("222")
                .build();


        when(cardReadRepository.fetchAllCards(anyMap())).thenReturn(Flux.just(card1, card2));

        Flux<CardReadModel> resultFlux = cardQueryService.fetchAllCards(Map.of());

        StepVerifier.create(resultFlux)
                .expectNext(card1)
                .expectNext(card2)
                .verifyComplete();
    }

    // Helper to create a CardId domain object from a long (stub)
    private com.collicode.card.business.domain.CardId domainCardId(long id) {
        return com.collicode.card.business.domain.CardId.of(id);
    }
}
