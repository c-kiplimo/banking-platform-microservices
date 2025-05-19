package com.collicode.card.business.application.outwardservice;

import com.collicode.card.business.domain.Card;
import com.collicode.card.business.domain.adapter.write.CardWriteAdapter;
import com.collicode.card.infrastructure.repository.model.write.CardWriteModel;
import com.collicode.card.infrastructure.repository.write.CardWriteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CardOutwardService implements CardWriteAdapter {
    private final CardWriteRepository cardWriteRepository;

    public CardOutwardService(CardWriteRepository cardWriteRepository) {
        this.cardWriteRepository = cardWriteRepository;
    }


    @Override
    public Mono<Card> createCard(Card card) {
        CardWriteModel cardWriteModel = CardWriteModel.builder()
                .recordId(card.getCardId().getCurrentId())
                .cardType(card.getCardType().name())
                .cvv(card.getCvv())
                .alias(card.getAlias())
                .accountId(card.getAccountId())
                .pan(card.getPan())
                .build();

        return cardWriteRepository.createCard(cardWriteModel)
                .thenReturn(card);
    }
}
