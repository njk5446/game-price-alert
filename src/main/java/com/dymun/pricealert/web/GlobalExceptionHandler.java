package com.dymun.pricealert.web;

import com.dymun.pricealert.domain.user.DuplicateEmailException;
import com.dymun.pricealert.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 모든 Controller의 예외를 한 곳에서 처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Service에서 던지는 "잘못된 요청" 관련 예외 (이메일 중복, 본인 확인 실패 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    // 이메일 중복 - 요청 자체는 정상이지만 기존 데이터와 충돌하는 상황
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)  // 409
                .body(new ErrorResponse(e.getMessage()));
    }

    // 로그인 시 비밀번호가 틀렸을 때
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)  // 401, 이미 이렇게 돼있었어요
                .body(new ErrorResponse("이메일 또는 비밀번호가 올바르지 않습니다"));
    }
}
