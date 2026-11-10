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
    void findOrCreateFromSearchResult_테스트() {
        // 1. 먼저 검색해서 결과를 얻음 (실제 화면에서는 사용자가 드롭다운에서 클릭하는 상황)
        List<ItadSearchResult> searchResults = itadClient.search("stardew valley");
        ItadSearchResult selected = searchResults.get(0);  // 첫 번째 결과 선택했다고 가정

        // 2. 그 결과로 등록
        Game first = gameService.findOrCreateFromSearchResult(selected);
        Game second = gameService.findOrCreateFromSearchResult(selected);

        System.out.println(first.getId());
        System.out.println(second.getId());
        System.out.println(first.getTitle());
    }

    @Test
    void searchGames_dlc제외하고game만나옴() {
        List<ItadSearchResult> results = gameService.searchGames("stard");

        for (ItadSearchResult r : results) {
            System.out.println(r.getTitle() + " / " + r.getType());
        }
    }
}
