package com.dtsvn.bookingmeeting.repository.notification;

import com.dtsvn.bookingmeeting.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
