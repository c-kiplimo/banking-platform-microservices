package com.collicode.card.infrastructure.service;

import com.collicode.card.infrastructure.repository.model.read.CardReadModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CardQueryService {
    Mono<CardReadModel> fetchCard(long cardId);

    Flux<CardReadModel> fetchAllCards(Map<String, String> filters);
}
