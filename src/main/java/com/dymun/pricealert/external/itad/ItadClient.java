package com.dymun.pricealert.external.itad;

import com.dymun.pricealert.config.ItadProperties;
import com.dymun.pricealert.external.itad.dto.ItadHistoryResult;
import com.dymun.pricealert.external.itad.dto.ItadLookupResponse;
import com.dymun.pricealert.external.itad.dto.ItadPriceResult;
import com.dymun.pricealert.external.itad.dto.ItadSearchResult;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

// ITAD API 호출하는 클라이언트
@Component
public class ItadClient {

    private final RestClient restClient;
    private final ItadProperties itadProperties;

    // API 호출 최대 재시도 횟수
    private static final int MAX_RETRY_COUNT = 3;

    // ITAD 가격 조회 API는 요청 1회당 게임 ID를 최대 200개까지만 받음 (ITAD API 쪽에서 명시한 제약)
    private static final int PRICE_CHUNK_SIZE = 200;

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
    // 찜한 게임들의 현재 가격을 ITAD에서 조회 (내 찜 목록 현재가 표시, 배치의 목표가 비교에 사용)
    // gameIds가 200개를 넘으면 200개씩 잘라서 가격 조회 요청을 여러 번 나눠 보내고, 결과를 합쳐서 반환
    public List<ItadPriceResult> prices(List<String> gameIds) {
        List<ItadPriceResult> allResults = new ArrayList<>();

        for (int i = 0; i < gameIds.size(); i += PRICE_CHUNK_SIZE) {
            int end = Math.min(i + PRICE_CHUNK_SIZE, gameIds.size());
            List<String> chunk = gameIds.subList(i, end);
            allResults.addAll(requestPricesWithRetry(chunk));
        }

        return allResults;
    }

    // 스팀 가격 이력(최근 3개월) 조회 (게임 상세 화면의 가격 추이 그래프용)
    public List<ItadHistoryResult> history(String itadId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games/history/v2")
                        .queryParam("key", itadProperties.getKey())
                        .queryParam("id", itadId)
                        .queryParam("country", "KR")
                        .queryParam("shops", 61)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<ItadHistoryResult>>() {});
    }

    // 게임 ID 200개 이하로 이루어진 chunk 하나의 가격을 ITAD 가격 조회 API로 실제 요청
    // 429가 오면 Retry-After만큼 기다렸다가 재시도, MAX_RETRY_COUNT만큼 계속 실패하면 포기하고 예외 발생
    private List<ItadPriceResult> requestPricesWithRetry(List<String> chunk) {
        int attempt = 0;

        while (true) {
            try {
                return restClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/games/prices/v3")
                                .queryParam("key", itadProperties.getKey())
                                .queryParam("country", "KR")
                                .build())
                        .body(chunk)
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<ItadPriceResult>>() {});
            } catch (HttpClientErrorException.TooManyRequests e) {
                attempt++;
                if (attempt >= MAX_RETRY_COUNT) {
                    throw new ItadApiException("가격 조회 재시도 횟수를 초과했습니다", e);
                }
                long waitSeconds = parseRetryAfterSeconds(e);
                sleep(waitSeconds);
            }
        }
    }

    // 429 응답에 담긴 Retry-After 헤더(가격 조회를 몇 초 후에 재시도하면 되는지) 값을 읽어서 숫자로 변환, 헤더가 없으면 기본값 5초
    private long parseRetryAfterSeconds(HttpClientErrorException.TooManyRequests e) {
        String retryAfter = e.getResponseHeaders() != null
                ? e.getResponseHeaders().getFirst("Retry-After")
                : null;
        if (retryAfter == null) {
            return 5;
        }
        return Long.parseLong(retryAfter);
    }

    // 가격 조회 재시도 전 대기하기 위한 Thread.sleep() 헬퍼
    private void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
