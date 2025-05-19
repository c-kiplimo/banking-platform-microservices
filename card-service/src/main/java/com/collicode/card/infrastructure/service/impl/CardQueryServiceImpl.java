package com.collicode.card.infrastructure.service.impl;

import com.collicode.card.infrastructure.repository.model.read.CardReadModel;
import com.collicode.card.infrastructure.repository.read.CardReadRepository;
import com.collicode.card.infrastructure.service.CardQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class CardQueryServiceImpl implements CardQueryService {
    private final CardReadRepository cardReadRepository;

    public CardQueryServiceImpl(CardReadRepository cardReadRepository) {
        this.cardReadRepository = cardReadRepository;
    }

    @Override
    public Mono<CardReadModel> fetchCard(long cardId) {
        return cardReadRepository.fetchCardById(cardId);
    }

    @Override
    public Flux<CardReadModel> fetchAllCards(Map<String, String> filters) {
        return cardReadRepository.fetchAllCards(filters);
    }
}
