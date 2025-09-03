package com.dtsvn.bookingmeeting.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomResponse;
import com.dtsvn.bookingmeeting.dto.user.UserResponse;
import com.dtsvn.bookingmeeting.dto.notification.NotificationResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for booking information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long id;

    private UserResponse createdBy;

    private MeetingRoomResponse meetingRoom;

    private String title;

    private String description;

    private String purpose;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime endTime;

    private BookingStatus status;

    private UserResponse approver;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime cancelledAt;

    private UserResponse cancelledBy;

    private String adminNotes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime updatedAt;

    private List<UserResponse> participants;
}
