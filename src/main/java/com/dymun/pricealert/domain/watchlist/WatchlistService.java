package com.dymun.pricealert.domain.watchlist;

import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 찜(Watchlist) 관리를 담당: 찜하기/취소 토글, 내 찜 목록 조회, 목표가 수정
@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    // 찜하기/찜취소 토글: 이미 찜한 상태면 취소, 아니면 목표가와 함께 새로 찜
    public boolean toggleWatch(User user, Game game, Integer targetPrice) {
        Optional<Watchlist> existing = watchlistRepository.findByUserAndGame(user, game);

        if (existing.isPresent()) {
             watchlistRepository.delete(existing.get());
            return false;
        } else {
            // 안 찜한 상태 -> 새로 찜
            Watchlist newWatchList = new Watchlist(user, game, targetPrice);
            watchlistRepository.save(newWatchList);
            return true;
        }
    }

    // 내 찜 목록 조회
    public List<Watchlist> getMyWatchList(User user) {
        return watchlistRepository.findByUser(user);
    }


    // 목표가 수정 (게임 상세화면): 본인의 찜만 수정 가능
    @Transactional
    public void updateTargetPrice(User user, Long watchListId, Integer newTargetPrice) {
        Watchlist watchlist = watchlistRepository.findById(watchListId)
                .orElseThrow(() -> new IllegalArgumentException("찜 항목을 찾을 수 없습니다."));

        if (!watchlist.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 찜만 수정할 수 있습니다.");
        }

        watchlist.changeTargetPrice(newTargetPrice);
    }

}
