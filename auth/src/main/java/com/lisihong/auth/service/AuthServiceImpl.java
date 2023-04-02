package com.lisihong.auth.service;

import com.lisihong.JWTResolver;
import com.lisihong.JWTUtils;
import com.lisihong.auth.config.TokenConstants;
import com.lisihong.auth.vr.AuthInfo;
import com.lisihong.auth.dao.UserRoleMapper;
import com.lisihong.auth.dtr.TokenPair;
import com.lisihong.auth.dao.UserMapper;
import com.lisihong.utils.RedisUtils;
import com.lisihong.utils.SnowFlakeIdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public record AuthServiceImpl(UserMapper userMapper, UserRoleMapper userRoleMapper,
                              RedisUtils redisUtils,
                              TokenConstants constants) implements AuthService {
    @Override
    public TokenPair login(AuthInfo authInfo, String location) {
        String username = authInfo.username();
        Integer id = userMapper.getIdByAuthInfo(authInfo.username(),
                authInfo.password());
        if (id == null)
            return null;
        String jti = String.valueOf(SnowFlakeIdGenerator.getId());
        Long change = redisUtils.hSet(username, location, jti);
        if (change == null)
            return null;
        if (change == 0) {
            change = redisUtils.hLen(username);
            if (change == null || change > constants.maxDevice())
                return null;
        }
        List<Integer> roleIds = userRoleMapper.getRoleIdsByUserId(id);
        return new TokenPair(
                JWTUtils.getToken(username, location
                        , roleIds, UUID.randomUUID().toString()
                        , constants.accessTokenExpirationTime()),
                JWTUtils.getToken(username, location
                        , roleIds, jti
                        , constants.refreshTokenExpirationTime()));
    }

    @Override
    public TokenPair refresh(String refreshToken) {
        JWTResolver resolver = new JWTResolver(refreshToken);
        String location = resolver.getLocation();
        String jti = resolver.getJWTId();
        String username = resolver.getUsername();
        if (jti == null) {
            return null;
        }
        String cacheJti = redisUtils
                .hGet(username, location);
        if (!jti.equals(cacheJti))
            return null;
        jti = String.valueOf(SnowFlakeIdGenerator.getId());
        redisUtils.hSet(username, location, jti);
        List<Integer> roleIds = resolver.getRoles();
        return new TokenPair(
                JWTUtils.getToken(username, location
                        , roleIds, UUID.randomUUID().toString()
                        , constants.accessTokenExpirationTime()),
                JWTUtils.getToken(username, location
                        , roleIds, jti
                        , constants.refreshTokenExpirationTime()));
    }

    @Override
    public boolean logout(String username, String location) {
        Long change = redisUtils.hDel(username, location);
        return change != null && change == 1;
    }
}
