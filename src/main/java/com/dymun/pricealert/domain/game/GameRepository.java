package com.dymun.pricealert.domain.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, Long> {

    // 자동완성 안 나올 시 스팀 링크로 해당 게임 등록 시 중복 확인
    Optional<Game> findBySteamAppid(Integer steamAppid);

    // 검색어 입력 후 자동완성된 게임들 중 클릭 후 이미 등록된 게임인지 중복 확인
    Optional<Game> findByItadId(UUID itadID);
}
