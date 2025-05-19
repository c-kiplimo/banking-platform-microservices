package com.collicode.card.infrastructure.repository.write;

import com.collicode.card.infrastructure.repository.model.write.CardWriteModel;
import reactor.core.publisher.Mono;

public interface CardWriteRepository {
    Mono<CardWriteModel> createCard(CardWriteModel cardWriteModel);
}
