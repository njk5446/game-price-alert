package com.dymun.pricealert.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 목표가 수정 요청
@Getter
@NoArgsConstructor
public class UpdateTargetPriceRequest {
    private Integer newTargetPrice;
}
