package com.dymun.pricealert.domain.watchlist;

import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    // "내 찜 목록" 화면 - 이 사용자가 찜한 전체 목록 조회
    List<Watchlist> findByUser(User user);

    // 찜 여부 확인 (토글 버튼, 배치 알림 처리)
    Optional<Watchlist> findByUserAndGame(User user, Game game);
}
