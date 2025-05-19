package com.collicode.card.business.domain.service.impl;

import com.collicode.card.business.domain.Card;
import com.collicode.card.business.domain.adapter.read.CardReadAdapter;
import com.collicode.card.business.domain.service.CardValidationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.collicode.card.business.domain.constants.CardConstants.ENTITYNAME;
import static com.collicode.shared.exception.BusinessEntityException.entityNotFoundException;
import static com.collicode.shared.exception.BusinessEntityException.invalidEntryException;
import static com.collicode.shared.exception.BusinessException.duplicateKeyException;

@Service
public class CardValidationServiceImpl implements CardValidationService {
    private final CardReadAdapter cardReadAdapter;

    public CardValidationServiceImpl(CardReadAdapter cardReadAdapter) {
        this.cardReadAdapter = cardReadAdapter;
    }

    @Override
    public Mono<Card> findAccountByCardId(long cardId) {
        return cardReadAdapter.findCardByCardId(cardId)
                .switchIfEmpty(Mono.error(entityNotFoundException(ENTITYNAME)));
    }

    @Override
    public Mono<Card> checkDuplicateCard(long cardId) {
        return cardReadAdapter.findCardByCardId(cardId)
                .flatMap(card -> Mono.error(duplicateKeyException(ENTITYNAME)));
    }

    @Override
    public Flux<Card> findCardsByAccountId(long accountId) {
        return cardReadAdapter.findCardsByAccountId(accountId);
    }


    @Override
    public Mono<Void> validateCardCreation(long accountId, String cardType) {
        return findCardsByAccountId(accountId)
                .collectList()
                .flatMap(cards -> {
                    if (cards.size() >= 2) {
                        return Mono.error(invalidEntryException(
                                ENTITYNAME,
                                "Account " + accountId + " already has maximum number of cards (2)."
                        ));
                    }
                    boolean duplicateType = cards.stream()
                            .anyMatch(card -> card.getCardType().name().equalsIgnoreCase(cardType));
                    if (duplicateType) {
                        return Mono.error(invalidEntryException(
                                ENTITYNAME,
                                "Account " + accountId + " already has a card of type " + cardType
                        ));
                    }
                    return Mono.empty();
                });
    }
}
