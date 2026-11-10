package com.dymun.pricealert.domain.watchlist;

import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "watchlist",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private Integer targetPrice;

    @Column(nullable = false)
    private Boolean isBelowTarget = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Watchlist(User user, Game game, Integer targetPrice) {
        this.user = user;
        this.game = game;
        this.targetPrice = targetPrice;
        this.isBelowTarget = false;
        this.createdAt = LocalDateTime.now();
    }

    // 목표가 변경 (게임 상세 화면에서 수정)
    public void changeTargetPrice(Integer newTargetPrice) {
        this.targetPrice = newTargetPrice;
    }
}
