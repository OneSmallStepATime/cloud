package com.lisihong.auth.service;

import com.lisihong.auth.dtr.TokenPair;
import com.lisihong.auth.vr.AuthInfo;

public interface AuthService {

    TokenPair login(AuthInfo authInfo, String location);

    TokenPair refresh(String refreshToken);

    boolean logout(String username, String location);
}
