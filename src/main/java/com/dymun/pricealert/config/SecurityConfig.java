package com.dymun.pricealert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/games/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll() // 회원가입/로그인은 누구나
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

    // 로그인 인증을 처리하는 관리자(AuthenticationManager) 등록
    // DB 조회(userDetailsService)와 비밀번호 비교(passwordEncoder)를 통해 인증 수행
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        // DB 기반 인증 처리 도구 - "이메일로 사용자를 찾고, 비밀번호를 비교"하는 역할
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        // 비밀번호 비교 시 사용할 암호화 도구 지정
        provider.setPasswordEncoder(passwordEncoder);

        // provider를 관리하는 매니저 생성 - 이게 실제 AuthenticationManager 역할을 함
        return new ProviderManager(provider);
    }
}
