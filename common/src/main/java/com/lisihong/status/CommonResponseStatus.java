package com.lisihong.status;

public enum CommonResponseStatus {
    AUTH_LOGIN_SUCCESS(0, "login success !"),
    AUTH_LOGOUT_SUCCESS(1, "logout success !"),
    AUTH_REFRESH_SUCCESS(2, "refresh success !"),
    AUTH_LOGIN_FAILED(3, "login failed !"),
    AUTH_LOGOUT_FAILED(4, "logout failed !"),
    AUTH_REFRESH_FAILED(5, "refresh failed !");

    private final int code;
    private final String message;

    CommonResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
