package com.dtsvn.bookingmeeting.dto.booking.client;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreateRequest {

    @NotNull(message = "Meeting room ID is required")
    private Long meetingRoomId;

    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String description;

    @Size(max = 500, message = "Purpose must not exceed 500 characters")
    private String purpose;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    private List<Long> participantIds;
}
