package com.dtsvn.bookingmeeting.service.notification;

import com.dtsvn.bookingmeeting.dto.notification.DeviceNotificationRequest;
import com.dtsvn.bookingmeeting.dto.notification.NotificationResponse;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.concurrent.ExecutionException;

public interface NotifcationService {
    public NotificationResponse sendNotificationToDevice(DeviceNotificationRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;
    public String markNotificationAsSeen(int notificationId) throws FirebaseMessagingException, ExecutionException, InterruptedException;
}
