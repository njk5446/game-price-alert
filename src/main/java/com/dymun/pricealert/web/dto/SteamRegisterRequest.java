package com.dymun.pricealert.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 스팀 링크로 게임 등록 요청
@Getter
@NoArgsConstructor
public class SteamRegisterRequest {
    private String steamUrl;
}
