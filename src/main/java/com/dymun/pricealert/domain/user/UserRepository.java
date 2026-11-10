package com.dymun.pricealert.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인 시, 사용자가 이메일 비번 입력 후 일치하면 로그인 성공
    Optional<User> findByEmail(String email);
}
