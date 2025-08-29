package com.dtsvn.bookingmeeting.dto.calendar;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO request cho room calendar API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCalendarRequest {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
}
