package com.collicode.card.business.domain.adapter.read;

import com.collicode.card.business.domain.Card;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardReadAdapter {
    Mono<Card> findCardByCardId(long cardId);

    Flux<Card> findCardsByAccountId(long accountId);
}
