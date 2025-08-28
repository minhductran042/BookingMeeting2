package com.dtsvn.bookingmeeting.service.notification;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.notification.DeviceNotificationRequest;
import com.dtsvn.bookingmeeting.dto.notification.NotificationRequest;
import com.dtsvn.bookingmeeting.dto.notification.NotificationResponse;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.concurrent.ExecutionException;

public interface NotifcationService {
    

    NotificationResponse sendNotificationToDevice(DeviceNotificationRequest request);

    void sendMeetingReminder(Booking booking, int minutesBefore);

    void sendBookingStatusUpdate(Booking booking, String status);

    void sendNotificationToUser(User user, NotificationRequest notificationRequest);
    
    void sendImmediateReminder(Long bookingId);
}
