package com.dtsvn.bookingmeeting.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO response cho lịch theo ngày - Đơn giản hóa theo mô hình thực tế
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyScheduleResponse {
    private LocalDate date;
    private String dayOfWeek;
    private List<DailyTimeSlotResponse> timeSlots;
    private int totalBookings; // Chỉ giữ tổng số booking
    private int totalRooms; // Tổng số phòng
    private List<DailyConflictResponse> conflicts; // Chỉ giữ xung đột
}
