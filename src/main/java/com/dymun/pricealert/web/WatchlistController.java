package com.dymun.pricealert.web;

import com.dymun.pricealert.domain.user.CustomUserDetails;
import com.dymun.pricealert.domain.user.User;
import com.dymun.pricealert.domain.watchlist.Watchlist;
import com.dymun.pricealert.domain.watchlist.WatchlistService;
import com.dymun.pricealert.web.dto.ToggleWatchRequest;
import com.dymun.pricealert.web.dto.UpdateTargetPriceRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Watchlist", description = "찜하기/찜 목록 관리 API")
@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @Operation(summary = "내 찜 목록 조회", description = "로그인한 사용자가 찜한 게임 목록을 조회")
    @GetMapping
    public List<Watchlist> getMyWatchList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return watchlistService.getMyWatchList(user);
    }

    // 찜하기/찜취소 토글: true면 새로 찜하기, false면 찜 취소
    @Operation(summary = "찜하기/찜취소", description = "이미 찜했으면 취소, 아니면 목표가와 함께 새로 찜하기")
    @PostMapping
    public boolean toggleWatch(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestBody ToggleWatchRequest req) {
        User user = userDetails.getUser();
        return watchlistService.toggleWatch(user, req.getGameId(), req.getTargetPrice());
    }

    @Operation(summary = "목표가 수정", description = "찜한 게임의 목표가를 수정")
    @PutMapping("/{watchlistId}")
    public void updateTargetPrice(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @PathVariable Long watchlistId,
                                  @RequestBody UpdateTargetPriceRequest req) {
        User user = userDetails.getUser();
        watchlistService.updateTargetPrice(user, watchlistId, req.getNewTargetPrice());
    }
}
