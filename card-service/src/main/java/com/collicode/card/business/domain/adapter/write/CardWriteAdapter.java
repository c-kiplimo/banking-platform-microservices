package com.collicode.card.business.domain.adapter.write;

import com.collicode.card.business.domain.Card;
import reactor.core.publisher.Mono;

public interface CardWriteAdapter {
    Mono<Card> createCard(Card card);
}
