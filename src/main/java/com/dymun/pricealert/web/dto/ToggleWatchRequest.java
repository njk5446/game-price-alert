package com.dymun.pricealert.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 찜하기/찜취소 토글 요청
@Getter
@NoArgsConstructor
public class ToggleWatchRequest {
    private Long gameId;
    private Integer targetPrice;
}
