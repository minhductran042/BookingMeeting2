package com.dtsvn.bookingmeeting.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO response cho xung đột lịch - Đơn giản hóa
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyConflictResponse {
    private String conflictType; // OVERLAP, DOUBLE_BOOKING
    private String description;
    private LocalDateTime conflictTime;
    private List<Long> conflictingBookingIds;
    private String severity; // HIGH, MEDIUM, LOW
}
