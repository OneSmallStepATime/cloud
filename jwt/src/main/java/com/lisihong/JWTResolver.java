package com.lisihong;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.List;

public record JWTResolver(DecodedJWT decodedJWT) {
    public JWTResolver(String token) {
        this(JWT.decode(token));
    }

    public boolean verify() {
        try {
            JWTVerifier verifier = JWT
                    .require(Algorithm.HMAC256(JWTUtils.SECRET_KEY))
                    .build();
            verifier.verify(decodedJWT);
            return true;
        } catch (Exception ex) {
            System.out.println("verify failed");
        }
        return false;
    }

    public String getUsername() {
        try {
            return decodedJWT.getSubject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Integer> getRoles() {
        try {
            return decodedJWT.getClaim("roles").asList(Integer.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getLocation() {
        try {
            return decodedJWT.getClaim("loc").asString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getJWTId() {
        try {
            return decodedJWT.getId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Long getExp() {
        try {
            return decodedJWT.getExpiresAt().getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
