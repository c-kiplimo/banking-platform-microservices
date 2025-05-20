package com.collicode.card.business.application.commandservice;

import com.collicode.card.business.domain.command.CardCommand;
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
import static com.collicode.card.business.application.commandservice.Constants.UPDATE;

@Service
@CommandType(entityName = CARD, action = UPDATE)
public class UpdateCardCommandHandler implements BusinessCommandHandler<CardRequest, CardResponse> {

    private final CardService cardService;

    public UpdateCardCommandHandler(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public Function<Command<CardRequest>, Mono<CommandResult<CardResponse>>> processCommand() {
        return command -> {
            CardRequest request = command.getPayload();

            CardCommand updateAliasCommand = toUpdateAliasCommand(request);

            return cardService.updateCard(updateAliasCommand)
                    .map(card -> CommandResult.<CardResponse>builder()
                            .commandId(command.getEntityName() + "_" + command.getAction())
                            .entityName(command.getEntityName())
                            .actionName(command.getAction())
                            .transactionId(command.getTrace().getTransactionId())
                            .result(toCardResponse(request))
                            .trace(command.getTrace())
                            .build());
        };
    }

    private CardCommand toUpdateAliasCommand(CardRequest request) {
        return CardCommand.builder()
                .cardId(request.getCardId())
                .alias(request.getAlias())
                .build();
    }

    private CardResponse toCardResponse(CardRequest request) {
        return CardResponse.builder()
                .cardId(request.getCardId())
                .alias(request.getAlias())
                .accountId(request.getAccountId())
                .cardType(request.getCardType())
                .pan(request.getPan())
                .cvv(request.getCvv())
                .build();
    }
}
