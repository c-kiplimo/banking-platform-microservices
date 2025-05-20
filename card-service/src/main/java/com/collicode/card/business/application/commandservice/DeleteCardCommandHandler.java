package com.collicode.card.business.application.commandservice;

import com.collicode.card.business.domain.service.CardService;
import com.collicode.card.infrastructure.dto.request.CardRequest;
import com.collicode.card.infrastructure.dto.response.CardResponse;
import com.collicode.shared.domain.api.CommandResult;
import com.collicode.shared.dto.Command;
import com.collicode.shared.service.BusinessCommandHandler;
import com.collicode.shared.service.CommandType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.collicode.card.business.application.commandservice.Constants.CARD;
import static com.collicode.card.business.application.commandservice.Constants.DELETE;

@Service
@CommandType(entityName = CARD, action = DELETE)
public class DeleteCardCommandHandler implements BusinessCommandHandler<CardRequest, CardResponse> {

    private final CardService cardService;

    public DeleteCardCommandHandler(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public Function<Command<CardRequest>, Mono<CommandResult<CardResponse>>> processCommand() {
        return command -> {
            long cardId = command.getPayload().getCardId();

            return cardService.deleteCard(cardId)
                    .map(deletedCard -> CommandResult.<CardResponse>builder()
                            .commandId(command.getEntityName() + "_" + command.getAction())
                            .entityName(command.getEntityName())
                            .actionName(command.getAction())
                            .transactionId(command.getTrace().getTransactionId())
                            .result(CardResponse.builder()
                                    .cardId(cardId)
                                    .build())
                            .trace(command.getTrace())
                            .build());
        };
    }
}

