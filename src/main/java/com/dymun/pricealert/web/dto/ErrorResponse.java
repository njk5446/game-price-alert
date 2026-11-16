package com.dymun.pricealert.web.dto;

import lombok.Getter;

// 에러 응답 공툥 형태
@Getter
public class ErrorResponse {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
