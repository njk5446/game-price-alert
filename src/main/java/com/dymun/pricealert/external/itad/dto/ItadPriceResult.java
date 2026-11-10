package com.dymun.pricealert.external.itad.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
[
    {
        "id": "018d937e-f835-710c-b95b-928e277b187e",
        "historyLow": {
            "all": { "amount": 8000, "amountInt": 8000, "currency": "KRW" },
            "y1": { "amount": 8000, "amountInt": 8000, "currency": "KRW" },
            "m3": { "amount": 11200, "amountInt": 11200, "currency": "KRW" }
        },
        "deals": [
            {
                "shop": { "id": 61, "name": "Steam" },
                "price": { "amount": 16000, "amountInt": 16000, "currency": "KRW" },
                "regular": { "amount": 16000, "amountInt": 16000, "currency": "KRW" },
                "cut": 0,
                "expiry": null,
                "url": "https://itad.link/018d9386-5b5b-..."
            }
        ]
    }
]
 */
// ITAD 가격 조회(prices) API 응답 - 게임 1개의 가격 정보 (List 형태로 응답)
@Getter
@NoArgsConstructor
public class ItadPriceResult {

    private String id; // ITAD 게임 ID
    private HistoryLow historyLow; // 역대 최저가 (전체/1년/3개월)
    private List<Deal> deals; // 스토어별 거래 조건 목록 (Steam, GOG 등)

    // 역대 최저가 정보
    @Getter
    @NoArgsConstructor
    public static class HistoryLow {
        private Price all; // 전체 기간 최저가
        private Price y1; // 최근 1년 최저가
        private Price m3; // 최근 3개월 최저가
    }

    // 스토어 하나의 거래 조건 (가격, 할인, 구매 링크 등)
    // 여러 스토어(Steam, PlaySum, Epic)가 있을 수 있음
    @Getter
    @NoArgsConstructor
    public static class Deal {
        private Shop shop; // 판매 스토어 (id, name)
        private Price price; // 현재가
        private Price regular; // 정가
        private Integer cut; // 할인율(%)
        private String expiry; // 할인 종료 시작 (할인 없으면 null)
        private String url; // 구매 링크 (itad.link 경유, 변경 금지)
    }

    // 판매 스토어 정보
    @Getter
    @NoArgsConstructor
    public static class Shop {
        private Integer id; // 스토어 ID (Steam=61, GOG=35, ...)
        private String name; // 스토어 이름
    }

    // 금액 정보 (JSON amount/amountInt/currency)
    @Getter
    @NoArgsConstructor
    public static class Price {
        private Integer amount; // 소수 금액 (예: USD 14.99)
        private Integer amountInt; // 통화 최소 단위 정수 (KRW는 amount와 동일, USD는 센트 단위)
        private String currency; // 통화 코드 (KRW, USD 등)
    }
}
