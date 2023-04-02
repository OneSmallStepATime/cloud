package com.lisihong.gateway.handlerImpl;

import com.lisihong.gateway.handler.ClientRequestHandler;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(0)
public record AuthHandlerImpl() implements ClientRequestHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().equals("/auth/login")) {
            return chain.filter(exchange);
        }
        return null;
    }
}
