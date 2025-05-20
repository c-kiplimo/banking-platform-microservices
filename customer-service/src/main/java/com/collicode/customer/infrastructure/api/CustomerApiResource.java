package com.collicode.customer.infrastructure.api;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Service
public class CustomerApiResource {

    @Bean(name = "customerApiRoute")
    public RouterFunction<ServerResponse> routes(CustomerApiHandler customerApiHandler) {
        return RouterFunctions
                .route(
                        POST(CustomerRoutes.CUSTOMER_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
                        customerApiHandler::createCustomer
                )
                .andRoute(
                        GET(CustomerRoutes.CUSTOMER_BY_ID).and(accept(MediaType.APPLICATION_JSON)),
                        customerApiHandler::fetchCustomerById
                )
                .andRoute(
                        GET(CustomerRoutes.CUSTOMER_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
                        customerApiHandler::fetchAllCustomers
                )
                .andRoute(
                        PUT(CustomerRoutes.CUSTOMER_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
                        customerApiHandler::updateCustomer
                )
                .andRoute(
                        DELETE(CustomerRoutes.CUSTOMER_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
                        customerApiHandler::deleteCustomer
                );
    }
}
