package com.dymun.pricealert.batch;

import com.dymun.pricealert.domain.notification.NotificationService;
import com.dymun.pricealert.domain.watchlist.Watchlist;
import com.dymun.pricealert.domain.watchlist.WatchlistRepository;
import com.dymun.pricealert.external.itad.ItadClient;
import com.dymun.pricealert.external.itad.dto.ItadPriceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 가격 확인 배치 로직: 찜한 게임들의 가격을 확인해서 목표가 도달 시 알림 발송
@Service
@RequiredArgsConstructor
public class PriceCheckService {

    private final WatchlistRepository watchlistRepository;
    private final ItadClient itadClient;
    private final NotificationService notificationService;

    @Transactional
    public void checkPrices() {
        // 1. 전체 찜 목록 가져오기
        List<Watchlist> allWatchlists = watchlistRepository.findAll();

        if (allWatchlists.isEmpty()) {
            return;
        }

        // 2. 찜한 게임들의 itad_id를 중복 없이 모으기
        List<String> itadIds = new ArrayList<>();
        for (Watchlist watchlist : allWatchlists) {
            String itadId = watchlist.getGame().getItadId().toString();
            if (!itadIds.contains(itadId)) {
                itadIds.add(itadId);
            }
        }

        // 3. ITAD에 한번에 가격 물어보기
        List<ItadPriceResult> priceResults = itadClient.prices(itadIds);

        // 4. 찜 목록을 하나씩 다 조회하고, 가격을 확인하고 처리
        for (Watchlist watchlist : allWatchlists) {
            String itadId = watchlist.getGame().getItadId().toString();

            // 4-1. priceResults 중에서, 이 게임에 해당하는 결과 찾기
            ItadPriceResult matchedResult = findMatchingResult(priceResults, itadId);
            if (matchedResult == null) {
                continue; // 못 찾으면 이 watchlist는 건너뜀
            }

            // 4-2. 그 결과에서 스팀 가격만 꺼내기
            Integer steamPrice = getSteamPrice(matchedResult);
            if (steamPrice == null) {
                continue; // 스팀 가격이 없으면 건너뜀
            }

            processWatchlist(watchlist, steamPrice);
        }

    }

    // priceResults 중에서 itadId가 일치하는 것을 찾는 메서드
    private ItadPriceResult findMatchingResult(List<ItadPriceResult> priceResults, String itadId) {
        for (ItadPriceResult result : priceResults) {
            if (result.getId().equals(itadId)) {
                return result;
            }
        }
        return null;
    }

    // deals 중에서 스팀(shop.id = 61)의 가격만 꺼내는 메서드
    private Integer getSteamPrice(ItadPriceResult priceResult) {
        for (ItadPriceResult.Deal deal : priceResult.getDeals()) {
            if (deal.getShop().getId() == 61) {
                return deal.getPrice().getAmountInt();
            }
        }
        return null;
    }

    // 목표가와 현재가를 비교해서, 알림 발송 여부와 상태(isBelowTarget)를 결정하는 메서드
    private void processWatchlist(Watchlist watchlist, Integer currentPrice) {
        boolean isNowBelowTarget = currentPrice <= watchlist.getTargetPrice();

        if (isNowBelowTarget && !watchlist.getIsBelowTarget()) {
            // 방금 목표가 이하로 떨어짐 -> 알림 발송
            // isNowBelowTarget = true (현재 가격이 목표가보다 싸다)
            // isBelowTarget = true (이전엔 목표가 이하가 아니었음)
            notificationService.saveNotification(watchlist.getUser(), watchlist.getGame(), currentPrice);
            watchlist.markBelowTarget();
        } else if (!isNowBelowTarget && watchlist.getIsBelowTarget()) {
            // 다시 목표가 위로 올라감 -> 상태만 초기화 (알림은 안 보냄)
            // isNowBelowTarget = false (현재가가 목표가보다 비싸다)
            // isBelowTarget = true (이전엔 목표가 이하였음, 즉 할인이 끝남)
            watchlist.markAboveTarget();
        }
    }
}
