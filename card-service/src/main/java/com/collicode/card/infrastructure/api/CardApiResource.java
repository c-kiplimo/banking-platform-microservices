package com.collicode.card.infrastructure.api;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Service
public class CardApiResource {
    @Bean(name = "cardApiRoute")
    public RouterFunction<ServerResponse> routes(CardApiHandler cardApiHandler) {
        return RouterFunctions
                .route(
                        POST(CardRoutes.CARD_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
                        cardApiHandler::createCard
                )
                .andRoute(
                        GET(CardRoutes.CARD_BY_ID).and(accept(MediaType.APPLICATION_JSON)),
                        cardApiHandler::fetchCardById
                )
                .andRoute(
                        GET(CardRoutes.CARD_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
                        cardApiHandler::fetchAllCards
                );
    }
}
