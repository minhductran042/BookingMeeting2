package com.dtsvn.bookingmeeting.service.notification;

import com.dtsvn.bookingmeeting.dto.notification.DeviceNotificationRequest;
import com.dtsvn.bookingmeeting.dto.notification.NotificationResponse;
import com.dtsvn.bookingmeeting.repository.notification.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
@Slf4j(topic = "Notification-Service")
public class NotificationServiceImpl implements NotifcationService {

    private final FirebaseApp firebaseApp;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String markNotificationAsSeen(int notificationId) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        return "";
    }

    @Override
    public NotificationResponse sendNotificationToDevice(DeviceNotificationRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        return null;
    }


}
