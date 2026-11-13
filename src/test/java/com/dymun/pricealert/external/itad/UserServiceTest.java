package com.dymun.pricealert.external.itad;

import com.dymun.pricealert.domain.user.User;
import com.dymun.pricealert.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void signUp_정상가입() {
        User user = userService.signUp("test4@example.com", "1234");

        System.out.println("저장된 email: " + user.getEmail());
        System.out.println("저장된 password: " + user.getPassword());

        // 저장된 password가 "1234" 그대로가 아니라 암호화됐는지 확인
        boolean matches = passwordEncoder.matches("1234", user.getPassword());
        System.out.println("비밀번호 일치 여부: " + matches);
    }
}
