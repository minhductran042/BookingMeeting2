package com.dtsvn.bookingmeeting.service.calendar.admin;

import com.dtsvn.bookingmeeting.dto.calendar.CalendarOverviewResponse;
import com.dtsvn.bookingmeeting.dto.calendar.CalendarRequest;
import com.dtsvn.bookingmeeting.dto.calendar.RoomCalendarRequest;

/**
 * Service interface cho admin calendar management
 */
public interface AdminCalendarService {

    /**
     * Lấy lịch tổng quan theo khoảng thời gian
     */
    CalendarOverviewResponse getCalendarOverview(CalendarRequest request);

    /**
     * Lấy lịch phòng cụ thể
     */
    CalendarOverviewResponse getRoomCalendar(Long roomId, RoomCalendarRequest request);
}
