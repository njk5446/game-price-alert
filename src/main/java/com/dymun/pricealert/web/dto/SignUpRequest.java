package com.dymun.pricealert.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원가입 요청 바디
@Getter
@NoArgsConstructor
public class SignUpRequest {
    private String email;
    private String password;
}
