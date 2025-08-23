package com.dtsvn.bookingmeeting.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomResponse;
import com.dtsvn.bookingmeeting.dto.user.UserResponse;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private BookingStatus status;

    private UserResponse approver;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelledAt;

    private UserResponse cancelledBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private List<UserResponse> participants;
}
