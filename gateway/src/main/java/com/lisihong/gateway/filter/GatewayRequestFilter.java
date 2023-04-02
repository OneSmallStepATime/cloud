package com.lisihong.gateway.filter;

import com.lisihong.gateway.handler.ClientRequestHandler;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public record GatewayRequestFilter(List<ClientRequestHandler> clientRequestHandlers)
        implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        for (ClientRequestHandler clientRequestHandler : clientRequestHandlers) {
            Mono<Void> result = clientRequestHandler.handle(exchange, chain);
            if (result != null) {
                return result;
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}