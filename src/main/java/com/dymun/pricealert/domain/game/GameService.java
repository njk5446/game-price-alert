package com.dymun.pricealert.domain.game;

import com.dymun.pricealert.external.itad.ItadClient;
import com.dymun.pricealert.external.itad.dto.ItadLookupResponse;
import com.dymun.pricealert.external.itad.dto.ItadSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    // 게임 등록(검색 경로): games 테이블에 없으면 검색 결과 그대로 저장 (ITAD lookup 추가 호출 없음)
    public Game findOrCreateFromSearchResult(ItadSearchResult searchResult) {
        UUID itadId = UUID.fromString(searchResult.getId());

        return gameRepository.findByItadId(itadId)
                .orElseGet(() -> {
                    Game newGame = new Game(
                            itadId,
                            null,
                            searchResult.getTitle(),
                            searchResult.getAssets().getBoxart(),
                            searchResult.getAssets().getBanner400()
                    );
                    return gameRepository.save(newGame);
                });
    }

    // 게임 검색 (자동완성): ITAD search 결과 중 game 타입만 반환 (dlc, package 등 제외)
    public List<ItadSearchResult> searchGames(String title) {
        return itadClient.search(title).stream()
                .filter(r -> "game".equals(r.getType()))
                .toList();
    }

}
