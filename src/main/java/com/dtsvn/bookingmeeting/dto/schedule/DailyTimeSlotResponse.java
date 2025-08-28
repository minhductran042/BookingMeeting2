package com.dtsvn.bookingmeeting.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

/**
 * DTO response cho time slot trong ngày - Đơn giản hóa
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyTimeSlotResponse {
    private LocalTime startTime;
    private LocalTime endTime;
    private List<DailyBookingResponse> bookings;
    private int availableRooms; // Chỉ giữ số phòng còn trống
    private String status; // AVAILABLE, PARTIALLY_BOOKED, FULLY_BOOKED
}
