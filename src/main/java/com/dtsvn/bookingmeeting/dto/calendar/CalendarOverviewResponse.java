package com.dtsvn.bookingmeeting.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarOverviewResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalBookings;
    private int totalRooms;
    private int totalLocations;
    private List<CalendarDayResponse> days;
}
