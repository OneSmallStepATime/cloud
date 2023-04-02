package com.lisihong.gateway.handler;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ClientRequestHandler {
    Mono<Void> handle(ServerWebExchange exchange, GatewayFilterChain chain);
}
