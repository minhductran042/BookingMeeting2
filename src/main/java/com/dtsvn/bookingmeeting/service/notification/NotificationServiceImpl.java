package com.dtsvn.bookingmeeting.service.notification;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.enumeration.NotificationType;
import com.dtsvn.bookingmeeting.domain.notification.Notification;
import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.notification.DeviceNotificationRequest;
import com.dtsvn.bookingmeeting.dto.notification.NotificationRequest;
import com.dtsvn.bookingmeeting.dto.notification.NotificationResponse;
import com.dtsvn.bookingmeeting.repository.booking.BookingRepository;
import com.dtsvn.bookingmeeting.repository.notification.NotificationRepository;
import com.dtsvn.bookingmeeting.repository.user.UserRepository;
import com.dtsvn.bookingmeeting.service.UserDevice.DeviceServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.dtsvn.bookingmeeting.domain.userDevice.UserDevice;

@Service
@AllArgsConstructor
@Slf4j(topic = "Notification-Service")
public class NotificationServiceImpl implements NotifcationService {

    private final NotificationRepository notificationRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FirebaseApp firebaseApp;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DeviceServiceImpl deviceServiceImpl;


    public void sendMeetingReminder(Booking booking, int minutesBefore) {
        try {
            String title = "Nhắc lịch họp";
            String message = String.format("Lịch họp '%s' sẽ bắt đầu sau %d phút",
                booking.getTitle(), minutesBefore);

            // Gửi thông báo cho người tạo
            sendNotificationToUser(booking.getCreatedBy(), title, message, booking);

            // Gửi thông báo cho tất cả người tham gia
            booking.getParticipants().forEach(participant -> {
                if (!participant.getUser().equals(booking.getCreatedBy())) {
                    sendNotificationToUser(participant.getUser(), title, message, booking);
                }
            });

            log.info("Sent meeting reminder for booking {} - {} minutes before",
                booking.getId(), minutesBefore);

        } catch (Exception e) {
            log.error("Error sending meeting reminder: {}", e.getMessage(), e);
        }
    }

    /**
     * Gửi thông báo cho user cụ thể
     */
    public void sendNotificationToUser(User user, String title, String message, Booking booking) {
        try {
            // Lưu notification vào database
            Notification notification = Notification.builder()
                    .user(user)
                    .booking(booking)
                    .title(title)
                    .message(message)
                    .type(NotificationType.BOOKING_REMINDER)
                    .read(false)
                    .build();

            notificationRepository.save(notification);
            log.info("Saved notification for user: {} - {}", user.getEmail(), title);

            // Tạo NotificationRequest với data phù hợp
            NotificationRequest notificationRequest = NotificationRequest.of(title, message)
                    .addData("type", "BOOKING_REMINDER")
                    .addData("bookingId", booking.getId())
                    .addData("minutesBefore", 0);

            // Gửi Firebase notification sử dụng NotificationRequest
            sendFirebaseNotificationToUser(user, notificationRequest);

        } catch (Exception e) {
            log.error("Error sending notification to user {}: {}", user.getEmail(), e.getMessage(), e);
        }
    }

    /**
     * Gửi notification sử dụng NotificationRequest (tiện ích)
     */
    public void sendNotificationToUser(User user, NotificationRequest notificationRequest) {
        try {
            // Lưu notification vào database
            Notification notification = Notification.builder()
                    .user(user)
                    .title(notificationRequest.getTitle())
                    .message(notificationRequest.getBody())
                    .type(NotificationType.GENERAL)
                    .read(false)
                    .build();

            notificationRepository.save(notification);
            log.info("Saved notification for user: {} - {}", user.getEmail(), notificationRequest.getTitle());

            // Gửi Firebase notification
            sendFirebaseNotificationToUser(user, notificationRequest);

        } catch (Exception e) {
            log.error("Error sending notification to user {}: {}", user.getEmail(), e.getMessage(), e);
        }
    }

    /**
     * Gửi Firebase push notification cho user (method cũ - giữ để tương thích)
     */
    private void sendFirebaseNotificationToUser(User user, String title, String message, Booking booking) {
        try {
            // Lấy device token từ UserDevice repository
            List<UserDevice> userDevices = deviceServiceImpl.getUserActiveDevices(user);

            for (UserDevice device : userDevices) {
                if (device.isActive() && device.getDeviceToken() != null) {
                    try {
                        Message fcmMessage = Message.builder()
                                .setToken(device.getDeviceToken())
                                .setNotification(
                                        com.google.firebase.messaging.Notification.builder()
                                                .setTitle(title)
                                                .setBody(message)
                                                .build()
                                )
                                .putData("bookingId", String.valueOf(booking.getId()))
                                .putData("type", "meeting_reminder")
                                .putData("notificationType", "BOOKING_REMINDER")
                                .build();

                        String response = FirebaseMessaging.getInstance(firebaseApp).sendAsync(fcmMessage).get();
                        log.info("Firebase notification sent to user {} device {}: {}",
                                user.getEmail(), device.getDeviceToken(), response);
                    } catch (Exception e) {
                        log.error("Error sending Firebase notification to device {}: {}", device.getDeviceToken(), e.getMessage());

                        // Nếu gửi thất bại, đánh dấu device không active
                        if (e instanceof FirebaseMessagingException) {
                            device.setActive(false);
                            log.warn("Device {} deactivated due to Firebase error", device.getDeviceToken());
                        }
                    }
                }
            }

            log.info("Firebase notification prepared for user: {} - {}", user.getEmail(), title);

        } catch (Exception e) {
            log.error("Error sending Firebase notification to user {}: {}", user.getEmail(), e.getMessage());
        }
    }

    private void sendFirebaseNotificationToUser(User user, NotificationRequest notificationRequest) {
        try {
            // Lấy device token từ UserDevice repository
            List<UserDevice> userDevices = deviceServiceImpl.getUserActiveDevices(user);

            for (UserDevice device : userDevices) {
                if (device.isActive() && device.getDeviceToken() != null) {
                    try {
                        Message.Builder messageBuilder = Message.builder()
                                .setToken(device.getDeviceToken())
                                .setNotification(
                                        com.google.firebase.messaging.Notification.builder()
                                                .setTitle(notificationRequest.getTitle())
                                                .setBody(notificationRequest.getBody())
                                                .setImage(notificationRequest.getImageUrl())
                                                .build()
                                );

                        // Thêm tất cả data từ notificationRequest
                        if (notificationRequest.getData() != null && !notificationRequest.getData().isEmpty()) {
                            for (Map.Entry<String, String> entry : notificationRequest.getData().entrySet()) {
                                messageBuilder.putData(entry.getKey(), entry.getValue());
                            }
                        }

                        Message fcmMessage = messageBuilder.build();
                        String response = FirebaseMessaging.getInstance(firebaseApp).sendAsync(fcmMessage).get();
                        log.info("Firebase notification sent to user {} device {}: {}",
                                user.getEmail(), device.getDeviceToken(), response);
                    } catch (Exception e) {
                        log.error("Error sending Firebase notification to device {}: {}", device.getDeviceToken(), e.getMessage());

                        // Nếu gửi thất bại, đánh dấu device không active
                        if (e instanceof FirebaseMessagingException) {
                            device.setActive(false);
                            log.warn("Device {} deactivated due to Firebase error", device.getDeviceToken());
                        }
                    }
                }
            }

            log.info("Firebase notification prepared for user: {} - {}", user.getEmail(), notificationRequest.getTitle());

        } catch (Exception e) {
            log.error("Error sending Firebase notification to user {}: {}", user.getEmail(), e.getMessage());
        }
    }

    /**
     * Scheduled task: Kiểm tra và gửi nhắc lịch họp
     * Chạy mỗi phút
     */
    @Scheduled(fixedRate = 60000) // 60 seconds = 1 minute
    public void checkAndSendMeetingReminders() {
        log.info("Checking for meeting reminders...");

        LocalDateTime now = LocalDateTime.now();

        // Lấy tất cả bookings đã approved và chưa bắt đầu
        List<Booking> approvedBookings = bookingRepository.findByStatus(
                com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus.APPROVED);

        // Lọc ra những booking chưa bắt đầu
        List<Booking> upcomingBookings = approvedBookings.stream()
                .filter(booking -> booking.getStartTime().isAfter(now))
                .collect(java.util.stream.Collectors.toList());

        for (Booking booking : upcomingBookings) {
            long minutesUntilStart = ChronoUnit.MINUTES.between(now, booking.getStartTime());

            // Gửi nhắc lịch 30 phút trước
            if (minutesUntilStart == 30) {
                sendMeetingReminder(booking, 30);
            }

            // Gửi nhắc lịch 15 phút trước
            if (minutesUntilStart == 15) {
                sendMeetingReminder(booking, 15);
            }

            // Gửi nhắc lịch 5 phút trước
            if (minutesUntilStart == 5) {
                sendMeetingReminder(booking, 5);
            }
        }
    }

        /**
     * Gửi nhắc lịch ngay lập tức (cho testing)
     */
    public void sendImmediateReminder(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        sendMeetingReminder(booking, 0);
    }


    public NotificationResponse sendNotificationToDevice(DeviceNotificationRequest request) {
        try {
            Message fcmMessage = Message.builder()
                    .setToken(request.getDeviceToken())
                    .setNotification(
                            com.google.firebase.messaging.Notification.builder()
                                    .setTitle(request.getTitle())
                                    .setBody(request.getBody())
                                    .setImage(request.getImageUrl())
                                    .build()
                    )
                    .putAllData(request.getData())
                    .build();

            String response = FirebaseMessaging.getInstance(firebaseApp).sendAsync(fcmMessage).get();
            log.info("sendNotificationToDevice response: {}", response);

            // Lưu notification vào database (không có user, chỉ lưu thông tin cơ bản)
            Notification notification = Notification.builder()
                    .title(request.getTitle())
                    .message(request.getBody())
                    .type(NotificationType.GENERAL)
                    .read(false)
                    .build();

            notificationRepository.save(notification);

            return NotificationResponse.builder()
                    .title(request.getTitle())
                    .body(request.getBody())
                    .tokenDevice(request.getDeviceToken())
                    .imageUrl(request.getImageUrl())
                    .sentAt(LocalDateTime.now())
                    .data(request.getData())
                    .isSeen(false)
                    .message("Notification sent successfully")
                    .build();

        } catch (Exception e) {
            log.error("Error sending notification to device: {}", e.getMessage(), e);
            return NotificationResponse.builder()
                    .title(request.getTitle())
                    .body(request.getBody())
                    .tokenDevice(request.getDeviceToken())
                    .imageUrl(request.getImageUrl())
                    .sentAt(LocalDateTime.now())
                    .data(request.getData())
                    .isSeen(false)
                    .message("Error sending notification: " + e.getMessage())
                    .build();
        }
    }
}
