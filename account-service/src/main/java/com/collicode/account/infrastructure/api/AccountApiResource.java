package com.collicode.account.infrastructure.api;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Service
public class AccountApiResource {
    @Bean(name = "accountApiRoute")
    public RouterFunction<ServerResponse> routes(AccountApiHandler accountApiHandler) {
        return RouterFunctions
                .route(
                        POST(AccountRoutes.ACCOUNT_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
                        accountApiHandler::createAccount
                )
                .andRoute(
                        GET(AccountRoutes.ACCOUNT_BY_ID).and(accept(MediaType.APPLICATION_JSON)),
                        accountApiHandler::fetchAccountById
                )
                .andRoute(
                        GET(AccountRoutes.ACCOUNT_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
                        accountApiHandler::fetchAllAccounts
                );
    }
}
