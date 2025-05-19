package com.collicode.card.infrastructure.repository.write.impl;

import com.collicode.card.infrastructure.repository.model.write.CardWriteModel;
import com.collicode.card.infrastructure.repository.write.CardWriteRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CardWriteRepositoryImpl implements CardWriteRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public CardWriteRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }


    @Override
    public Mono<CardWriteModel> createCard(CardWriteModel cardWriteModel) {
        return r2dbcEntityTemplate.insert(cardWriteModel);
    }
}
