package com.collicode.card.business.domain.service;

import com.collicode.card.business.domain.Card;
import com.collicode.card.business.domain.command.CardCommand;
import reactor.core.publisher.Mono;

public interface CardService {
    Mono<Card> createCard(CardCommand cardCommand);

    Mono<Card> updateCard(CardCommand cardCommand);

    Mono<Card> deleteCard(long cardId);
}
