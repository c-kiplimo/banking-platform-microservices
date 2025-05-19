package com.collicode.card.business.application.commandservice;

import com.collicode.card.business.domain.command.CardCommand;
import com.collicode.card.business.domain.service.CardService;
import com.collicode.card.business.domain.valueobject.CardType;
import com.collicode.card.infrastructure.client.AccountClient;
import com.collicode.card.infrastructure.dto.request.CardRequest;
import com.collicode.card.infrastructure.dto.response.CardResponse;
import com.collicode.shared.domain.api.CommandResult;
import com.collicode.shared.dto.Command;
import com.collicode.shared.exception.BusinessEntityException;
import com.collicode.shared.service.BusinessCommandHandler;
import com.collicode.shared.service.CommandType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.collicode.card.business.application.commandservice.Constants.CARD;
import static com.collicode.card.business.application.commandservice.Constants.CREATE;

@Service
@CommandType(entityName = CARD, action = CREATE)
public class CreateCardCommandHandler implements BusinessCommandHandler<CardRequest, CardResponse> {

    private final CardService cardService;
    private final AccountClient accountClient;

    public CreateCardCommandHandler(CardService cardService, AccountClient accountClient) {
        this.cardService = cardService;
        this.accountClient = accountClient;
    }

    @Override
    public Function<Command<CardRequest>, Mono<CommandResult<CardResponse>>> processCommand() {
        return command -> {
            CardRequest request = command.getPayload();

            // Reactive call to get Account by ID and validate existence
            return accountClient.getAccountById(request.getAccountId())
                    .switchIfEmpty(Mono.error(
                            BusinessEntityException.entityNotFoundException("Invalid Account ID: " + request.getAccountId())
                    ))
                    .flatMap(accountResponse -> {
                        CardCommand cardCommand = toCreateCardCommand(request);

                        return cardService.createCard(cardCommand)
                                .map(card -> CommandResult.<CardResponse>builder()
                                        .commandId(command.getEntityName() + "_" + command.getAction())
                                        .entityName(command.getEntityName())
                                        .actionName(command.getAction())
                                        .transactionId(command.getTrace().getTransactionId())
                                        .result(toCardResponse(request).withCardId(card.getCardId().getCurrentId()))
                                        .trace(command.getTrace())
                                        .build());
                    });
        };
    }

    private CardCommand toCreateCardCommand(CardRequest request) {
        return CardCommand.builder()
                .alias(request.getAlias())
                .accountId(request.getAccountId())
                .cardType(CardType.valueOf(request.getCardType()))
                .pan(request.getPan())
                .cvv(request.getCvv())
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
