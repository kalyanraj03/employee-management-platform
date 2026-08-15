package com.emp.apigateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserIdentityFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        return exchange.getPrincipal()
                .flatMap(principal -> {

                    if (principal instanceof JwtAuthenticationToken jwtAuthenticationToken) {

                        String keycloakUserId =
                                jwtAuthenticationToken
                                        .getToken()
                                        .getSubject();

                        ServerHttpRequest request =
                                exchange.getRequest()
                                        .mutate()
                                        .header(
                                                "X-User-Keycloak-Id",
                                                keycloakUserId
                                        )
                                        .build();

                        return chain.filter(
                                exchange.mutate()
                                        .request(request)
                                        .build()
                        );
                    }

                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}