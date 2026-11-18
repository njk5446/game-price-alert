package com.dymun.pricealert.domain.game;

import com.dymun.pricealert.external.itad.ItadClient;
import com.dymun.pricealert.external.itad.dto.ItadHistoryResult;
import com.dymun.pricealert.external.itad.dto.ItadLookupResponse;
import com.dymun.pricealert.external.itad.dto.ItadPriceResult;
import com.dymun.pricealert.external.itad.dto.ItadSearchResult;
import com.dymun.pricealert.web.dto.GameDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 게임(Game) 조회/등록을 담당: 스팀 링크/검색 경로로 games 테이블에 등록하고, 자동완성 검색 결과를 제공
@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final ItadClient itadClient;

    // 게임 등록(steam 경로): games 테이블에 없으면 ITAD lookup으로 조회 후 저장
    public Game findOrCreateBySteamAppid(Integer steamAppid) {
        return gameRepository.findBySteamAppid(steamAppid)
                .orElseGet(() -> {
                    ItadLookupResponse response = itadClient.lookup(steamAppid);

                    if (!response.isFound()) {
                        throw new IllegalArgumentException("해당 게임을 찾을 수 없습니다");
                    }

                    UUID itadId = UUID.fromString(response.getGame().getId());

                    // itad_id로 한번 더 확인 (검색 경로로 이미 등록된 게임일 수 있다)
                    return gameRepository.findByItadId(itadId)
                            .orElseGet(() -> {
                                Game newGame = new Game(
                                        itadId,
                                        steamAppid,
                                        response.getGame().getTitle(),
                                        response.getGame().getAssets().getBoxart(),
                                        response.getGame().getAssets().getBanner400()
                                );
                                return gameRepository.save(newGame);
                            });
                });
    }

    // 게임 등록(검색 경로): 요청에서 받은 필드로 바로 등록
    public Game findOrCreateFromSearchRequest(String itadIdStr, String title, String boxart, String banner400) {
        UUID itadId = UUID.fromString(itadIdStr);

        return gameRepository.findByItadId(itadId)
                .orElseGet(() -> {
                    Game newGame = new Game(itadId, null, title, boxart, banner400);
                    return gameRepository.save(newGame);
                });
    }


    // 게임 검색 (자동완성): ITAD search 결과 중 game 타입만 반환 (dlc, package 등 제외)
    public List<ItadSearchResult> searchGames(String title) {
        return itadClient.search(title).stream()
                .filter(r -> "game".equals(r.getType()))
                .toList();
    }

    // 스팀 링크 전체를 받아서, appid 추출 후 등록까지 한 번에 처리
    public Game findOrCreateBySteamUrl(String steamUrl) {
        Integer appid = extractAppId(steamUrl);
        return findOrCreateBySteamAppid(appid);
    }

    // 스팀 링크에서 appid 추출 (예: .../app/413150/... -> 413150)
    private Integer extractAppId(String steamUrl) {
        Pattern pattern = Pattern.compile("/app/(\\d+)");
        Matcher matcher = pattern.matcher(steamUrl);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalArgumentException("올바른 게임 링크가 아닙니다.");
    }

    // 게임 상세조회: 현재가 + 역대 최저가 + 가격 추이를 ITAD에서 가져와서 응답으로 조립
    public GameDetailResponse getGameDetail(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다."));

        String itadId = game.getItadId().toString();

        List<ItadPriceResult> priceResults = itadClient.prices(List.of(itadId));
        List<ItadHistoryResult> historyResults = itadClient.history(itadId);

        ItadPriceResult priceResult = priceResults.get(0); // 상세화면에선 itadId 하나만 가져오니까 첫번째값만 있어서 0으로 가져옴

        return new GameDetailResponse(
                game.getTitle(),
                game.getBoxArtUrl(),
                priceResult.getSteamPrice(),
                priceResult.getHistoryLow(),
                historyResults
        );
    }

}
