package com.dymun.pricealert.external.itad;

import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.game.GameRepository;
import com.dymun.pricealert.domain.game.GameService;
import com.dymun.pricealert.external.itad.dto.ItadSearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ItadClient itadClient;

    @Test
    void findOrCreateBySteamAppid_두번째호출시_재사용됨() {
        // 같은 appid로 두 번 호출
        Game first = gameService.findOrCreateBySteamAppid(413150);
        Game second = gameService.findOrCreateBySteamAppid(413150);

        // 같은 DB id를 가졌는지 확인 (= 새로 안 만들고 재사용했다는 뜻)
        System.out.println(first.getId());
        System.out.println(second.getId());
    }

    @Test
    void searchGames_dlc제외하고game만나옴() {
        List<ItadSearchResult> results = gameService.searchGames("stard");

        for (ItadSearchResult r : results) {
            System.out.println(r.getTitle() + " / " + r.getType());
        }
    }
}
