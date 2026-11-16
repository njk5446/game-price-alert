package com.dymun.pricealert.web;

import com.dymun.pricealert.domain.user.UserService;
import com.dymun.pricealert.web.dto.LoginRequest;
import com.dymun.pricealert.web.dto.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 회원가입/로그인 API
@Tag(name = "Auth", description = "회원가입/로그인 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "회원가입", description = "이메일과 비밀번호로 회원가입합니다.")
    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpRequest req) {
        userService.signUp(req.getEmail(), req.getPassword());
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest req, HttpServletRequest httpRequest) {
        // 이메일+비밀번호로 인증 시도 (틀리면 예외 발생)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        // 인증 성공 결과를 SecruityContext에 담음
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 세션에 저장 -> 이후 요청마다 로그인 상태로 인식됨
        httpRequest.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context
        );
    }

}
