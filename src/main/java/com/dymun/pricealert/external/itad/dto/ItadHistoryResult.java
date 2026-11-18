package com.dymun.pricealert.external.itad.dto;

import lombok.Getter;

import java.time.OffsetDateTime;

// ITAD 가격 이력(history) API 응답
// 매일의 가격이 아니라, 가격이 실제로 바뀐 시점마다 하나씩 찍힌 로그
// 이 리스트를 시간순으로 이으면 가격 추이 그래프가 됨
@Getter
public class ItadHistoryResult {

    private OffsetDateTime timestamp; // 가격이 이 값으로 바뀐 시점 (현재가격)
    private Shop shop; // 어느 스토어에서 바뀐 가격인지
    private Deal deal; // 그 시점부터


    @Getter
    public static class Shop {
        private Integer id;
        private String name;
    }

    // 그 시점의 할인가/정가/할인율
    @Getter
    public static class Deal {
        private Price price; // 그 시점의 판매가(할인중이면 할인가, 아니면 정가와 동일)
        private Price regular; // 정가(할인 여부와 무관하게 원래 가격)
        private Integer cut; // 할인율(%), 할인 아니면 0
    }

    // 통화 포함 가격 값
    @Getter
    public static class Price {
        private Double amount; // 가격 (소수점 표현, 예: 11.99)
        private Integer amountInt; // 가격 (정수 표현)
        private String currency; // 통화 코드 (예: "KRW")
    }
}
