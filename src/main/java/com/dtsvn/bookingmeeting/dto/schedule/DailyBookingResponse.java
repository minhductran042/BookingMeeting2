package com.dtsvn.bookingmeeting.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO response cho booking trong daily schedule - Đơn giản hóa
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyBookingResponse {
    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String roomName;
    private String createdBy;
    private String color; // Màu theo status
}
