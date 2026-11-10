package com.dymun.pricealert.external.itad.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
/* 게임 정보 JSON 응답 객체
{
    "found": true,
    "game": {
        "id": "018d937e-f835-710c-b95b-928e277b187e",
        "slug": "stardew-valley",
        "title": "Stardew Valley",
        "type": "game",
        "mature": false,
        "assets": {
            "boxart": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/boxart.jpg?t=1786566930",
            "banner145": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/banner145.jpg?t=1786566930",
            "banner300": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/banner300.jpg?t=1786566930",
            "banner400": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/banner400.jpg?t=1786566930",
            "banner600": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/banner600.jpg?t=1786566930"
        }
    }
}

// 해당 steamAppid로 등록된 게임이 없는 경우
{
    "found": false
}
*/
// ITAD 게임 조회(lookup) API 응답 - steamAppid로 ITAD 게임 ID를 찾을 때 사용
public class ItadLookupResponse {

    private boolean found; // 찾았는지 여부
    private GameInfo game; // 찾은 게임 정보 (found가 true일 때만 유효)

    // 게임 기본 정보
    @Getter
    @NoArgsConstructor
    public static class GameInfo {
        private String id; // ITAD 게임 ID (UUID) - 가격/이력 조회에 사용
        private String title; // 게임 제목 (일단 영어만)
        private Assets assets; // 이미지 정보
    }

    // 게임 이미지 주소
    @Getter
    @NoArgsConstructor
    public static class Assets {
        private String boxart; // 박스아트 (목록/카드용 이미지)
        private String banner400; // 배너 (상세 화면용 이미지)
    }
}
