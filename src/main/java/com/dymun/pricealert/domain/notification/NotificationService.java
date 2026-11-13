package com.dymun.pricealert.domain.notification;

import com.dymun.pricealert.domain.game.Game;
import com.dymun.pricealert.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 알림 발송 기록을 저장: 배치가 목표가 도달을 확인하고 알림을 보낼 때 기록 남김
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void saveNotification(User user, Game game, Integer priceAtNotification) {
        Notification notification = new Notification(user, game, priceAtNotification);
        notificationRepository.save(notification);
    }
}
