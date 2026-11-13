package com.dymun.pricealert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // Spring Secuirty 보안 기능 활성화
public class SecurityConfig {

    // 비밀번호 암호화/검증용 인코더 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화도구 (업계표준)
    }

    // 요청 경로별 접근 권한 설정 (어떤 요청은 로그인 없이, 어떤 요청은 로그인 필요)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF(위조요청방지) 보호를 끔. 보통 React처럼 프론트 백엔드가 분리된 구조에서는 끄고 토큰 방식으로 보안을 처리
                .formLogin(form -> form.disable()) // 기본 로그인 폼 끄기 (React가 화면 담당)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 회원가입/로그인은 누구나
                        .anyRequest().authenticated() // 나머지는 로그인 필요
                );

        return http.build();
    }

    // React(localhost:3000)에 오는 요청을 허용하는 CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // 리액트 개발 서버 포트
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // 해당 메서드 방식 허용
        config.setAllowedHeaders(List.of("*")); // 요청에 어떤 헤더(Header)를 담아 보내도 괜찮다
        config.setAllowCredentials(true); // 쿠키(세션 정보)를 포함한 요청/응답을 허용

        // 경로별 CORS 규칙을 관리할 창구 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로(/**)에 config에 설정한 규칙을 적용하도록 등록
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
