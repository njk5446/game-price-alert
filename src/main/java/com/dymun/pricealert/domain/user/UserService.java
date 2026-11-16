package com.dymun.pricealert.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // 회원가입: 이메일 중복 체크 후 비밀번호 암호화해서 저장
    public User signUp(String email, String rawPassword) {
        // 1. 이메일 중복 체크
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("이미 가입된 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 3. User 생성 및 저장
        User newUser = new User(email, encodedPassword);
        return userRepository.save(newUser);
    }

    // 로그인 인증 처리
    public Authentication login(String email, String  rawPassword) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );
    }
}
