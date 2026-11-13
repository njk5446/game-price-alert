package com.dymun.pricealert.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Spring Security가 요구하는 사용자 형태(UserDetails)로 우리 User를 변환하는 어댑터
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 로그인 식별자 - 우리는 이메일로 로그인하므로 email 반환
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 저장된 암호화 비밀번호 (로그인 시 입력값과 비교용)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 권한/역할 목록 - 관리자 구분 등 없어서 빈 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // 계정 만료 여부 - 만료 기능 없음, 항상 true
    @Override
    public boolean isAccountNonExpired() { return true; }

    // 계정 잠금 여부 - 잠금 기능 없음, 항상 true
    @Override
    public boolean isAccountNonLocked() { return true; }

    // 비밀번호 만료 여부 - 만료 기능 없음, 항상 true
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    // 계정 활성화 여부 - 탈퇴 기능 없음, 항상 true
    @Override
    public boolean isEnabled() { return true; }

    // Controller 등에서 원본 User 정보가 필요할 때 사용
    public User getUser() {
        return user;
    }
}