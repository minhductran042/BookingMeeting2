package com.dtsvn.bookingmeeting.web.rest.calendar.client;

import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.calendar.CalendarOverviewResponse;
import com.dtsvn.bookingmeeting.dto.calendar.CalendarRequest;
import com.dtsvn.bookingmeeting.service.calendar.client.ClientCalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar/client")
@RequiredArgsConstructor
@Slf4j
public class ClientCalendarController {

    private final ClientCalendarService clientCalendarService;
    @PostMapping("/my")
    public ApiResponse<CalendarOverviewResponse> getMyCalendar(@RequestBody @Valid CalendarRequest request) {
        log.info("Getting my calendar with request: {}", request);
        try {
            CalendarOverviewResponse response = clientCalendarService.getMyCalendar(request);
            return new ApiResponse<>(200, "My calendar fetched successfully", response);
        } catch (Exception e) {
            log.error("Error getting my calendar", e);
            return new ApiResponse<>(400, "Failed to get my calendar: " + e.getMessage(), null);
        }

    }
}
