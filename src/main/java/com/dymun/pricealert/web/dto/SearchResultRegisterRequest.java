package com.dymun.pricealert.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 검색 결과로 게임 등록 요청 (사용자가 자동완성에서 선택한 게임)
@Getter
@NoArgsConstructor
public class SearchResultRegisterRequest {
    private String itadId;
    private String title;
    private String boxart;
    private String banner400;
}
