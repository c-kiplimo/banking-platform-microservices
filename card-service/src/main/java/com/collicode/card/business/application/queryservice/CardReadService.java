package com.collicode.card.business.application.queryservice;

import com.collicode.card.business.domain.Card;
import com.collicode.card.business.domain.CardId;
import com.collicode.card.business.domain.adapter.read.CardReadAdapter;
import com.collicode.card.business.domain.valueobject.CardType;
import com.collicode.card.infrastructure.repository.read.CardReadRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardReadService implements CardReadAdapter {
    private final CardReadRepository cardReadRepository;

    public CardReadService(CardReadRepository cardReadRepository) {
        this.cardReadRepository = cardReadRepository;
    }

    @Override
    public Mono<Card> findCardByCardId(long cardId) {
        return cardReadRepository.fetchCardById(cardId)
                .map(cardReadModel -> Card.builder()
                        .cardId(CardId.of(cardReadModel.getRecordId()))
                        .cardType(CardType.valueOf(cardReadModel.getCardType()))
                        .cvv(cardReadModel.getCvv())
                        .alias(cardReadModel.getAlias())
                        .accountId(cardReadModel.getAccountId())
                        .pan(cardReadModel.getPan())
                        .build());
    }

    @Override
    public Flux<Card> findCardsByAccountId(long accountId) {
        return cardReadRepository.fetchCardsByAccountId(accountId)
                .map(cardReadModel -> Card.builder()
                        .cardId(CardId.of(cardReadModel.getRecordId()))
                        .cardType(CardType.valueOf(cardReadModel.getCardType()))
                        .cvv(cardReadModel.getCvv())
                        .alias(cardReadModel.getAlias())
                        .accountId(cardReadModel.getAccountId())
                        .pan(cardReadModel.getPan())
                        .build());
    }

}
