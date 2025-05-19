package com.collicode.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("account-service", r -> r.path("/accounts/**")
                        .filters(f -> f.rewritePath("/accounts/(?<segment>.*)", "/api/v1/accounts/${segment}"))
                        .uri("lb://account-service"))
                .route("card-service", r -> r.path("/cards/**")
                        .filters(f -> f.rewritePath("/cards/(?<segment>.*)", "/api/v1/cards/${segment}"))
                        .uri("lb://card-service"))
                .route("customer-service", r -> r.path("/customers/**")
                        .filters(f -> f.rewritePath("/customers/(?<segment>.*)", "/api/v1/customers/${segment}"))
                        .uri("lb://customer-service"))
                .build();
    }
}
