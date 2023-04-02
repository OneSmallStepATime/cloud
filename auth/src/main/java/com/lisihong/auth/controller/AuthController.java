package com.lisihong.auth.controller;

import com.lisihong.auth.service.AuthService;
import com.lisihong.auth.dtr.TokenPair;
import com.lisihong.auth.vr.AuthInfo;
import com.lisihong.response.CommonBodyResponse;
import com.lisihong.response.CommonHeaderResponse;
import com.lisihong.status.CommonResponseStatus;
import com.lisihong.utils.CommonResponseBuilder;
import com.lisihong.utils.IpResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public record AuthController(AuthService authService) {
    @PostMapping("login")
    public ResponseEntity<CommonBodyResponse<TokenPair>>
    login(@RequestBody AuthInfo authInfo, HttpServletRequest request) {
        String location = request.getHeader("mac");
        if (location == null) {
            location = IpResolver.getIpAddress(request);
        }
        TokenPair tokenPair = authService
                .login(authInfo, location);
        if (tokenPair == null) {
            return CommonResponseBuilder.body(HttpStatus.UNAUTHORIZED
                    , CommonResponseStatus.AUTH_LOGIN_FAILED
                    , null);
        } else {
            return CommonResponseBuilder.body(HttpStatus.OK
                    , CommonResponseStatus.AUTH_LOGIN_SUCCESS
                    , tokenPair);
        }
    }

    @GetMapping("refresh")
    public ResponseEntity<CommonBodyResponse<TokenPair>> refresh(String refreshToken) {
        TokenPair tokenPair = authService.refresh(refreshToken);
        if (tokenPair == null) {
            return CommonResponseBuilder.body(HttpStatus.UNAUTHORIZED
                    , CommonResponseStatus.AUTH_REFRESH_FAILED
                    , null);
        } else {
            return CommonResponseBuilder.body(HttpStatus.OK
                    , CommonResponseStatus.AUTH_REFRESH_SUCCESS
                    , tokenPair);
        }
    }

    @GetMapping("logout")
    public ResponseEntity<CommonHeaderResponse> logout(HttpServletRequest request) {
        String username = request.getHeader(HttpHeaders.AUTHORIZATION);
        String location = request.getHeader("mac");
        if (location == null) {
            location = IpResolver.getIpAddress(request);
        }
        return authService.logout(username, location) ?
                CommonResponseBuilder.header(HttpStatus.OK
                        , CommonResponseStatus.AUTH_LOGOUT_SUCCESS) :
                CommonResponseBuilder.header(HttpStatus.UNAUTHORIZED
                        , CommonResponseStatus.AUTH_LOGOUT_FAILED);
    }

    @GetMapping("test")
    public ResponseEntity<CommonHeaderResponse> test() {
        return null;
    }
}
