package com.dymun.pricealert.external.itad;

import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.game.GameService;
import com.dymun.pricealert.domain.notification.NotificationService;
import com.dymun.pricealert.domain.user.User;
import com.dymun.pricealert.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameService gameService;

    @Test
    void saveNotification_정상저장() {
        User user = userRepository.save(new User("notiftest@example.com", "1234"));
        Game game = gameService.findOrCreateBySteamAppid(413150);

        notificationService.saveNotification(user, game, 9900);

        System.out.println("알림 저장 완료");
    }


}
