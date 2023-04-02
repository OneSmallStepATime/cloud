package com.lisihong.gateway.handlerImpl;

import com.lisihong.JWTResolver;
import com.lisihong.gateway.handler.ClientRequestHandler;
import com.lisihong.gateway.sync.cache.LocalCache;
import com.lisihong.gateway.utils.GatewayResponseWrapper;
import com.lisihong.gateway.utils.IpResolver;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@Order(1)
public record TokenHandlerImpl()
        implements ClientRequestHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders httpHeaders = request.getHeaders();
        String token = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null || token.isEmpty()) {
            return GatewayResponseWrapper.unauthorizedResponse(exchange.getResponse());
        }
        JWTResolver resolver = new JWTResolver(token);
        String tokenLocation = resolver.getLocation();
        String realLocation = httpHeaders.getFirst("mac");
        if (realLocation == null) {
            realLocation = IpResolver.getIpAddress(request);
        }
        Long exp = resolver.getExp();
        List<Integer> ownerRoleIds = resolver.getRoles();
        if (tokenLocation == null || !tokenLocation.equals(realLocation)
                || exp == null || exp < System.currentTimeMillis()
                || !resolver.verify() || ownerRoleIds == null) {
            return GatewayResponseWrapper.unauthorizedResponse(exchange.getResponse());
        }
        boolean pass = false;
        Map<Integer, Set<Integer>> requiredLicenseIdsMap =
                LocalCache.apiLicenseMap.get(request.getURI().getPath());
        if (requiredLicenseIdsMap == null || requiredLicenseIdsMap.isEmpty()) {
            pass = true;
        } else {
            HashSet<Integer> ownerLicenseIds = new HashSet<>();
            for (Integer roleId : ownerRoleIds) {
                ownerLicenseIds.addAll(LocalCache.roleLicenseMap.get(roleId));
            }
            Collection<Set<Integer>> requiredLicenseIds = requiredLicenseIdsMap.values();
            outer:
            for (Set<Integer> requiredLicenseIdSet : requiredLicenseIds) {
                for (Integer requiredLicenseId : requiredLicenseIdSet) {
                    if (!ownerLicenseIds.contains(requiredLicenseId))
                        continue outer;
                }
                pass = true;
                break;
            }
        }
        if (pass) {
            exchange.mutate().request(request.mutate().headers(headers ->
                    headers.set(HttpHeaders.AUTHORIZATION
                            , resolver.getUsername())).build()).build();
            return null;
        }
        return GatewayResponseWrapper.unauthorizedResponse(exchange.getResponse());
    }
}
