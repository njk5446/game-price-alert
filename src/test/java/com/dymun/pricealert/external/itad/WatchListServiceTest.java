package com.dymun.pricealert.external.itad;

import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.game.GameService;
import com.dymun.pricealert.domain.user.User;
import com.dymun.pricealert.domain.user.UserRepository;
import com.dymun.pricealert.domain.watchlist.Watchlist;
import com.dymun.pricealert.domain.watchlist.WatchlistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WatchListServiceTest {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;

    @Test
    void toggleWatch_찜하고_다시누르면_취소됨() {
        // 테스트용 User, Game 준비
        User user = userRepository.save(new User("test@example.com", "1234"));
        Game game = gameService.findOrCreateBySteamAppid(413150);

        // 처음 toggleWatch → 찜됨(true)이어야 함
        boolean first = watchlistService.toggleWatch(user, game, 10000);
        System.out.println("첫 번째 호출: " + first);

        // 다시 toggleWatch → 찜 취소(false)여야 함
        boolean second = watchlistService.toggleWatch(user, game, 10000);
        System.out.println("두 번째 호출: " + second);

        // 내 찜 목록 확인 (취소됐으니 비어있어야 함)
        List<Watchlist> myList = watchlistService.getMyWatchList(user);
        System.out.println("찜 목록 개수: " + myList.size());
    }

    @Test
    void updateTargetPrice_본인찜만수정가능() {
        User user = userRepository.save(new User("test3@example.com", "1234"));
        Game game = gameService.findOrCreateBySteamAppid(413150);

        watchlistService.toggleWatch(user, game, 10000);  // 먼저 찜

        Watchlist watchlist = watchlistService.getMyWatchList(user).get(0);
        watchlistService.updateTargetPrice(user, watchlist.getId(), 8000);

        Watchlist updated = watchlistService.getMyWatchList(user).get(0);
        System.out.println("수정된 목표가: " + updated.getTargetPrice());
    }
}
