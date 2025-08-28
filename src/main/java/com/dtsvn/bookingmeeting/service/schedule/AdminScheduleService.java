package com.dtsvn.bookingmeeting.service.schedule;

import com.dtsvn.bookingmeeting.dto.schedule.DailyScheduleResponse;

import java.time.LocalDate;

/**
 * Service interface cho admin schedule management
 */
public interface AdminScheduleService {
    DailyScheduleResponse getDailySchedule(LocalDate date, Long locationId);
}
