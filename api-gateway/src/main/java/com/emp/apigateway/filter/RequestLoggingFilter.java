package com.emp.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(
            org.springframework.web.server.ServerWebExchange exchange,
            GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        System.out.println(
                ">>> GATEWAY REQUEST: "
                        + request.getMethod()
                        + " "
                        + request.getURI()
        );

        System.out.println(
                ">>> AUTHORIZATION HEADER PRESENT: "
                        + request.getHeaders().containsHeader("Authorization")
        );

        return chain.filter(exchange)
                .doFinally(signal ->
                        System.out.println(
                                "<<< GATEWAY RESPONSE: "
                                        + exchange.getResponse().getStatusCode()
                        )
                );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}