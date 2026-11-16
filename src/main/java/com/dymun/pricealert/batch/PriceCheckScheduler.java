package com.dymun.pricealert.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 가격 확인 배치를 "언제" 실행할지 담당
// @Component로 빈 등록해야 스프링이 이 클래스의 @Scheduled 메서드를 찾을 수 있음
@Component
@RequiredArgsConstructor
public class PriceCheckScheduler {

    private final PriceCheckService priceCheckService;

//    @Scheduled(cron = "*/10 * * * * *") // 매 10초마다 테스트용으로
    @Scheduled(cron = "0 0 * * * *") // 매시 정각
    public void runPriceCheck() {
//        System.out.println("가격 체크 배치 실행됨 - " + java.time.LocalDateTime.now());
        priceCheckService.checkPrices();
    }
}
