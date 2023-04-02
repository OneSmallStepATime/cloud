package com.lisihong.utils;

import com.lisihong.response.CommonHeaderResponse;
import com.lisihong.status.CommonResponseStatus;
import com.lisihong.response.CommonBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record CommonResponseBuilder() {

    public static <T> ResponseEntity<CommonBodyResponse<T>> body(
            HttpStatus s, CommonResponseStatus crs, T data) {
        return ResponseEntity.status(s)
                .body(new CommonBodyResponse<>(crs.getCode(), crs.getMessage(), data));
    }

    public static ResponseEntity<CommonHeaderResponse> header(
            HttpStatus s, CommonResponseStatus crs) {
        return ResponseEntity.status(s)
                .body(new CommonHeaderResponse(crs.getCode(), crs.getMessage()));
    }
}
