package com.dtsvn.bookingmeeting.dto.booking.client;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String title;

    private String description;

    private String purpose;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime endTime;

    private List<Long> participantIds;
}
