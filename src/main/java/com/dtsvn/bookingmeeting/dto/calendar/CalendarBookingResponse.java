package com.dtsvn.bookingmeeting.dto.calendar;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarBookingResponse {
    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String roomName;
    private String locationName;
    private String createdBy;
    private String color; // Màu sắc để hiển thị trên lịch
}
