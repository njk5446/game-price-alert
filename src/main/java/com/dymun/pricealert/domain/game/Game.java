package com.dymun.pricealert.domain.game;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "games")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID itadId;

    @Column(unique = true)
    private Integer steamAppid;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String boxArtUrl;

    @Column(length = 500)
    private String bannerUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Game(UUID itadId, Integer steamAppid, String title, String boxArtUrl, String bannerUrl) {
        this.itadId = itadId;
        this.steamAppid = steamAppid;
        this.title = title;
        this.boxArtUrl = boxArtUrl;
        this.bannerUrl = bannerUrl;
        this.createdAt = LocalDateTime.now();
    }

}
