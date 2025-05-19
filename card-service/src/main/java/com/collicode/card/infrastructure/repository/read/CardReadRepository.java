package com.collicode.card.infrastructure.repository.read;

import com.collicode.card.infrastructure.repository.model.read.CardReadModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CardReadRepository {
    Mono<CardReadModel> fetchCardById(long cardId);

    Flux<CardReadModel> fetchCardsByAccountId(long accountId);

    Flux<CardReadModel> fetchAllCards(Map<String, String> filters);
}
