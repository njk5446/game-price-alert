package com.dymun.pricealert.domain.notification;

import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

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
    private Integer priceAtNotification;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    public Notification (User user, Game game, Integer priceAtNotification) {
        this.user = user;
        this.game = game;
        this.priceAtNotification = priceAtNotification;
        this.sentAt = LocalDateTime.now();
    }
}
