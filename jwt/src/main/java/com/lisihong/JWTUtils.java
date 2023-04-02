package com.lisihong;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.List;

public record JWTUtils() {
    public static final String SECRET_KEY = "ReBackCYa38407";

    public static String getToken(String username, String location
            , List<Integer> roleIds, String JWTId, long exp) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + exp))
                .withClaim("loc", location)
                .withClaim("roles", roleIds)
                .withJWTId(JWTId)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
}