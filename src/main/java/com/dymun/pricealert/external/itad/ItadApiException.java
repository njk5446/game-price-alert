package com.dymun.pricealert.external.itad;

// ITAD Open API 에러 트래킹을 위한 예외처리
public class ItadApiException extends RuntimeException{
    public ItadApiException(String message) {
        super(message);
    }

    public ItadApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
