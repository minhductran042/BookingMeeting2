package com.dtsvn.bookingmeeting.service.calendar.client;

import com.dtsvn.bookingmeeting.dto.calendar.CalendarOverviewResponse;
import com.dtsvn.bookingmeeting.dto.calendar.CalendarRequest;

public interface ClientCalendarService {
    CalendarOverviewResponse getMyCalendar(CalendarRequest request);
}
