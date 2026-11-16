package com.dymun.pricealert.web;


import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.game.GameService;
import com.dymun.pricealert.external.itad.dto.ItadSearchResult;
import com.dymun.pricealert.web.dto.SearchResultRegisterRequest;
import com.dymun.pricealert.web.dto.SteamRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Game", description = "게임 검색/등록 API")
@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    // 게임 검색 (자동완성): 제목으로 ITAD 검색, game 타입만 반환
    @Operation(summary = "게임 검색", description = "제목으로 게임을 검색 (자동완성)")
    @GetMapping("/search")
    public List<ItadSearchResult> search(@RequestParam String title) {
        return gameService.searchGames(title);
    }

    @Operation(summary = "스팀 링크로 게임 등록", description = "스팀 상점 링크를 붙여넣어 게임을 등록")
    @PostMapping("/register/steam")
    public Game registerBySteamUrl(@RequestBody SteamRegisterRequest req) {
        return gameService.findOrCreateBySteamUrl(req.getSteamUrl());
    }

    @Operation(summary = "검색 결과로 게임 등록", description = "자동완성에서 선택한 게임 정보로 등록")
    @PostMapping("/register/search")
    public Game registerFromSearchResult(@RequestBody SearchResultRegisterRequest req) {
        return gameService.findOrCreateFromSearchRequest(
                req.getItadId(),
                req.getTitle(),
                req.getBoxart(),
                req.getBanner400()
        );
    }


}
