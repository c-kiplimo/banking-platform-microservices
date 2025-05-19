package com.collicode.card.business.domain.service;

import com.collicode.card.business.domain.Card;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardValidationService {
    Mono<Card> findAccountByCardId(long cardId);

    Mono<Card> checkDuplicateCard(long cardId);


    Flux<Card> findCardsByAccountId(long accountId);


    Mono<Void> validateCardCreation(long accountId, String cardType);

}
