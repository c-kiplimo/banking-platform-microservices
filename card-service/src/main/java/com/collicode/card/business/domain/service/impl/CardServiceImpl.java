package com.collicode.card.business.domain.service.impl;

import com.collicode.card.business.domain.Card;
import com.collicode.card.business.domain.CardId;
import com.collicode.card.business.domain.adapter.write.CardWriteAdapter;
import com.collicode.card.business.domain.command.CardCommand;
import com.collicode.card.business.domain.service.CardService;
import com.collicode.card.business.domain.service.CardValidationService;
import com.collicode.shared.domain.service.SequenceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.collicode.card.business.domain.constants.CardConstants.ENTITYNAME;


@Service
public class CardServiceImpl implements CardService {

    private final CardValidationService cardValidationService;
    private final CardWriteAdapter cardWriteAdapter;
    private final SequenceRepository<CardId, Long, Long> sequenceRepository;

    public CardServiceImpl(CardValidationService cardValidationService,
                           CardWriteAdapter cardWriteAdapter,
                           SequenceRepository<CardId, Long, Long> sequenceRepository) {
        this.cardValidationService = cardValidationService;
        this.cardWriteAdapter = cardWriteAdapter;
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public Mono<Card> createCard(CardCommand cardCommand) {
        return identity()
                .flatMap(cardId -> createCard(Card.from(cardCommand, cardId)));
    }

    public Mono<Card> createCard(Card card) {
        return cardValidationService.validateCardCreation(card.getAccountId(), card.getCardType().name())
                .then(cardValidationService.checkDuplicateCard(card.getCardId().getCurrentId()))
                .switchIfEmpty(cardWriteAdapter.createCard(card))
                .cast(Card.class);
    }


    public Mono<CardId> identity() {
        return sequenceRepository.findPreviousId(ENTITYNAME)
                .map(identity -> CardId.of(identity.getCurrentId(), identity.getFactor()).nextId())
                .flatMap(cardId -> sequenceRepository.updateId(ENTITYNAME, cardId.getCurrentId())
                        .thenReturn(cardId))
                .switchIfEmpty(sequenceRepository.insertId(
                                ENTITYNAME,
                                CardId.initialId().getCurrentId(),
                                CardId.initialId().getFactor()
                        )
                        .thenReturn(CardId.initialId()));
    }
}
