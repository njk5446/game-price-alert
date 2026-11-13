package com.dymun.pricealert.domain.notification;

import com.dymun.pricealert.domain.watchlist.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
