package com.dymun.pricealert.web.dto;

import com.dymun.pricealert.external.itad.dto.ItadHistoryResult;
import com.dymun.pricealert.external.itad.dto.ItadPriceResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

// 게임 상세 화면 응답 - 현재가, 역대 최저가, 가격 추이를 한 번에 내려줌
@Getter
@RequiredArgsConstructor
public class GameDetailResponse {
    private final String title;
    private final String boxart;
    private final Integer currentPrice;
    private final ItadPriceResult.HistoryLow historyLow;
    private final List<ItadHistoryResult> priceHistory;
}
