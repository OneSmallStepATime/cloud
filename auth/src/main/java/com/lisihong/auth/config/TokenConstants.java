package com.lisihong.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record TokenConstants(Long accessTokenExpirationTime,
                             Long refreshTokenExpirationTime,
                             Integer maxDevice) {
}
