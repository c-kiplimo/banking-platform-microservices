package com.collicode.card.infrastructure.api;

import com.collicode.card.infrastructure.dto.request.CardRequest;
import com.collicode.card.infrastructure.service.CardQueryService;
import com.collicode.card.util.ResponseHandler;
import com.collicode.shared.domain.api.ApiRequest;
import com.collicode.shared.domain.api.AuditInfo;
import com.collicode.shared.domain.api.BusinessCommandRouterService;
import com.collicode.shared.domain.api.CommandWrapper;
import com.collicode.shared.util.JsonHelper;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.collicode.card.infrastructure.api.CardConstants.CARD;
import static com.collicode.card.infrastructure.api.CardConstants.CREATE;


@Service
public class CardApiHandler {
    private final BusinessCommandRouterService businessCommandRouterService;
    private final CardQueryService cardQueryService;

    public CardApiHandler(BusinessCommandRouterService businessCommandRouterService, CardQueryService cardQueryService) {
        this.businessCommandRouterService = businessCommandRouterService;
        this.cardQueryService = cardQueryService;
    }


    public Mono<ServerResponse> createCard(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);

        return serverRequest.bodyToMono(String.class)
                .flatMap(requestBody -> {
                    TypeToken<ApiRequest<CardRequest>> typeToken = new TypeToken<>() {
                    };
                    ApiRequest<CardRequest> request = JsonHelper.toObject(requestBody, typeToken.getType());

                    CommandWrapper<CardRequest> commandWrapper = CommandWrapper.<CardRequest>builder()
                            .entityName(CARD)
                            .actionName(CREATE)
                            .auditInfo(auditInfo)
                            .withOriginalApiRequest(request)
                            .build();

                    return businessCommandRouterService
                            .processCommand(commandWrapper);
                })
                .transform(resultMono -> ResponseHandler.handleCommandResultMonoResponse(resultMono, auditInfo));
    }

    public Mono<ServerResponse> fetchCardById(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);
        long cardId = Long.parseLong(serverRequest.pathVariable("cardId"));

        return ResponseHandler.handleMonoResponse(
                cardQueryService.fetchCard(cardId),
                auditInfo
        );
    }

    public Mono<ServerResponse> fetchAllCards(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);

        Map<String, String> filters = serverRequest.queryParams()
                .toSingleValueMap();

        return ResponseHandler.handleFluxResponse(
                cardQueryService.fetchAllCards(filters),
                auditInfo
        );
    }

}
