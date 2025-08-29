package com.dtsvn.bookingmeeting.dto.calendar;

import lombok.*;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDayResponse {
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private int totalBookings;
    private int availableRooms;
    private List<CalendarBookingResponse> bookings;
    private String status; // AVAILABLE, BUSY, FULL
}
