package com.lisihong.gateway.utils;

import com.alibaba.fastjson2.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public record GatewayResponseWrapper() {

    public static Mono<Void> unauthorizedResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type"
                , "application/json;charset=UTF-8");
        return response.writeWith(Flux
                .just(response
                        .bufferFactory()
                        .wrap(JSON
                                .toJSONBytes(
                                        new GatewayResponse(666
                                                , "invalid token !")
                                ))));
    }

    record GatewayResponse(int code, String message) {
    }
}
