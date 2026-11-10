package com.dymun.pricealert.external.itad;

import com.dymun.pricealert.config.ItadProperties;
import com.dymun.pricealert.external.itad.dto.ItadLookupResponse;
import com.dymun.pricealert.external.itad.dto.ItadPriceResult;
import com.dymun.pricealert.external.itad.dto.ItadSearchResult;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

// ITAD API 호출하는 클라이언트
@Component
public class ItadClient {

    private final RestClient restClient;
    private final ItadProperties itadProperties;

    public ItadClient(ItadProperties itadProperties) {
        this.itadProperties = itadProperties;
        this.restClient = RestClient.create(itadProperties.getBaseUrl());
    }

    // 스팀 appid로 ITAD 게임 ID 조회 (스팀 링크 등록 경로에서 사용)
    public ItadLookupResponse lookup(Integer steamAppid) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games/lookup/v1")
                        .queryParam("key", itadProperties.getKey())
                        .queryParam("appid", steamAppid)
                        .build())
                .retrieve()
                .body(ItadLookupResponse.class);
    }

    // 게임 제목으로 검색 (자동완성, 영어만 지원, 부분 검색 가능)
    public List<ItadSearchResult> search(String title) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games/search/v1")
                        .queryParam("key", itadProperties.getKey())
                        .queryParam("title", title)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<ItadSearchResult>>() {});

    }

    /*
    요청:
    [
        "018d937f-012f-73b8-ab2c-898516969e6a",   ← 첫 번째 게임
        "01849782-1017-7389-8de4-c97c587fd7e3"    ← 두 번째 게임
    ]

    응답:
    [
        { "id": "018d937f-012f-73b8-ab2c-898516969e6a", ... },  ← 첫 번째 게임의 가격
        { "id": "01849782-1017-7389-8de4-c97c587fd7e3", ... }   ← 두 번째 게임의 가격
    ]
     */
    // 여러 게임의 가격을 한 번에 조회 (게임 ID 배열로 요청, 최대 200개)
    // 내 찜 목록 화면의 현재가 표시, 배치의 목표가 비교에 사용
    public List<ItadPriceResult> prices(List<String> gameIds) {
        return restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/games/prices/v3")
                        .queryParam("key", itadProperties.getKey())
                        .queryParam("country", "KR")
                        .build())
                .body(gameIds)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ItadPriceResult>>() {});
    }
}
