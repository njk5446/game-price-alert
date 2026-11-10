package com.dymun.pricealert.external.itad.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
/*
[
    {
        "id": "018d937e-f835-710c-b95b-928e277b187e",
        "slug": "stardew-valley",
        "title": "Stardew Valley",
        "type": "game",
        "mature": false,
        "assets": {
            "banner145": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/banner145.jpg?t=1786566930",
            "banner300": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/banner300.jpg?t=1786566930",
            "banner400": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/banner400.jpg?t=1786566930",
            "banner600": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/banner600.jpg?t=1786566930",
            "boxart": "https://assets.isthereanydeal.com/018d937e-f835-710c-b95b-928e277b187e/boxart.jpg?t=1786566930"
        }
    },
    {
        "id": "018d937f-33fa-722b-9933-86fb37dfb136",
        "slug": "stardrive",
        "title": "StarDrive",
        "type": "game",
        "mature": false,
        "assets": {}
    }
]
 */
// ITAD 게임 검색 (search) API 응답 - 제목으로 게임 찾을 때 사용 (List 형태로 응답)
public class ItadSearchResult {

    private String id; // ITAD 게임 ID (UUID)
    private String title; // 게임 제목 (일단 영어만)
    private String type; // game/dlc/package 등 - "game"만 필터링해서 사용
    private Assets assets; // 이미지 정보 (비어 있을 수 있음)

    // 게임 이미지 주소
    @Getter
    @NoArgsConstructor
    public static class Assets {
        private String boxart; // 박스아트 (목록/카드용 이미지)
        private String banner400; // 배너 (상세 화면용 이미지)
    }
}
