//package com.dtsvn.bookingmeeting.web.rest.notification;
//
//import com.dtsvn.bookingmeeting.dto.ApiResponse;
//import com.dtsvn.bookingmeeting.dto.notification.DeviceNotificationRequest;
//import com.dtsvn.bookingmeeting.dto.notification.NotificationRequest;
//import com.dtsvn.bookingmeeting.dto.notification.NotificationResponse;
//import com.dtsvn.bookingmeeting.service.notification.NotifcationService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping("/api/notifications")
//@Tag(name = "Notification", description = "Notification management APIs")
//public class NotificationController {
//
//    private final NotifcationService notificationService;
//
//    @PostMapping("/device")
//    @Operation(summary = "Send notification to specific device")
//    public ResponseEntity<ApiResponse<NotificationResponse>> sendNotificationToDevice(
//            @Valid @RequestBody DeviceNotificationRequest request) {
//        log.info("Sending notification to device: {}", request.getDeviceToken());
//        try {
//            NotificationResponse response = notificationService.sendNotificationToDevice(request);
//            return ResponseEntity.ok(ApiResponse.<NotificationResponse>builder()
//                    .status(200)
//                    .message("Notification sent successfully")
//                    .data(response)
//                    .build());
//        } catch (Exception e) {
//            log.error("Error sending notification to device: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(ApiResponse.<NotificationResponse>builder()
//                    .status(400)
//                    .message("Error sending notification: " + e.getMessage())
//                    .build());
//        }
//    }
//
//
//    @PostMapping("/reminder/{bookingId}")
//    @Operation(summary = "Send immediate meeting reminder for testing")
//    public ResponseEntity<ApiResponse<String>> sendImmediateReminder(@PathVariable Long bookingId) {
//        log.info("Sending immediate reminder for booking: {}", bookingId);
//        try {
//            notificationService.sendImmediateReminder(bookingId);
//            return ResponseEntity.ok(ApiResponse.<String>builder()
//                    .status(200)
//                    .message("Reminder sent successfully")
//                    .data("Reminder sent for booking ID: " + bookingId)
//                    .build());
//        } catch (Exception e) {
//            log.error("Error sending immediate reminder: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
//                    .status(400)
//                    .message("Error sending reminder: " + e.getMessage())
//                    .build());
//        }
//    }
//
//    @PostMapping("/check-reminders")
//    @Operation(summary = "Manually check and send meeting reminders")
//    public ResponseEntity<ApiResponse<String>> checkAndSendReminders() {
//        log.info("Manually checking for meeting reminders");
//        try {
//            notificationService.sendMeetingReminder();
//            return ResponseEntity.ok(ApiResponse.<String>builder()
//                    .status(200)
//                    .message("Meeting reminders checked and sent successfully")
//                    .data("Reminders processed")
//                    .build());
//        } catch (Exception e) {
//            log.error("Error checking meeting reminders: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
//                    .status(400)
//                    .message("Error checking reminders: " + e.getMessage())
//                    .build());
//        }
//    }
//}
