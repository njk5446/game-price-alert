package com.dymun.pricealert.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 요청 바디
@Getter
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
