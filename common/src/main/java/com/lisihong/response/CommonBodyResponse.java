package com.lisihong.response;
public record CommonBodyResponse<T>(int code, String message, T data) {
}
