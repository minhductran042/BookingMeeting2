package com.dtsvn.bookingmeeting.web.rest.calendar.admin;

import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.calendar.*;
import com.dtsvn.bookingmeeting.service.calendar.admin.AdminCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/calendar/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Calendar", description = "APIs cho admin calendar management")
public class AdminCalendarController {

    private final AdminCalendarService adminCalendarService;

    @PostMapping
    @Operation(summary = "Lấy lịch tổng quan", description = "Lấy lịch tổng quan theo khoảng thời gian")
    public ApiResponse<CalendarOverviewResponse> getCalendarOverview(
            @Valid @RequestBody CalendarRequest request) {

        log.info("Getting calendar overview with request: {}", request);

        try {
            CalendarOverviewResponse response = adminCalendarService.getCalendarOverview(request);
            return new ApiResponse<>(200, "Calendar overview fetched successfully", response);
        } catch (Exception e) {
            log.error("Error getting calendar overview", e);
            return new ApiResponse<>(400, "Failed to get calendar overview: " + e.getMessage(), null);
        }
    }

    @PostMapping("/room/{roomId}")
    @Operation(summary = "Lấy lịch phòng cụ thể", description = "Lấy lịch của một phòng cụ thể theo khoảng thời gian")
    public ApiResponse<CalendarOverviewResponse> getRoomCalendar(
            @Parameter(description = "ID phòng họp", example = "1")
            @PathVariable Long roomId,
            @Valid @RequestBody RoomCalendarRequest request) {

        log.info("Getting room calendar for roomId: {}, with request: {}", roomId, request);

        try {
            CalendarOverviewResponse response = adminCalendarService.getRoomCalendar(roomId, request);
            return new ApiResponse<>(200, "Room calendar fetched successfully", response);
        } catch (Exception e) {
            log.error("Error getting room calendar for roomId: {}", roomId, e);
            return new ApiResponse<>(400, "Failed to get room calendar: " + e.getMessage(), null);
        }
    }

    // Giữ lại GET endpoints để backward compatibility
    @GetMapping("/calendar")
    @Operation(summary = "Lấy lịch tổng quan (GET)", description = "Lấy lịch tổng quan theo khoảng thời gian - Legacy endpoint")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CalendarOverviewResponse> getCalendarOverviewLegacy(
            @Parameter(description = "Ngày bắt đầu", example = "2024-09-01")
            @RequestParam String startDate,
            @Parameter(description = "Ngày kết thúc", example = "2024-09-30")
            @RequestParam String endDate,
            @Parameter(description = "ID location (optional)", example = "1")
            @RequestParam(required = false) Long locationId) {

        log.info("Getting calendar overview (legacy) from {} to {}, locationId: {}", startDate, endDate, locationId);

        try {
            // Convert string dates to LocalDate
            java.time.LocalDate start = java.time.LocalDate.parse(startDate);
            java.time.LocalDate end = java.time.LocalDate.parse(endDate);

            CalendarRequest request = CalendarRequest.builder()
                .startDate(start)
                .endDate(end)
                .locationId(locationId)
                .build();

            CalendarOverviewResponse response = adminCalendarService.getCalendarOverview(request);
            return new ApiResponse<>(200, "Calendar overview fetched successfully", response);
        } catch (Exception e) {
            log.error("Error getting calendar overview (legacy)", e);
            return new ApiResponse<>(400, "Failed to get calendar overview: " + e.getMessage(), null);
        }
    }

    @GetMapping("/calendar/room/{roomId}")
    @Operation(summary = "Lấy lịch phòng cụ thể (GET)", description = "Lấy lịch của một phòng cụ thể theo khoảng thời gian - Legacy endpoint")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CalendarOverviewResponse> getRoomCalendarLegacy(
            @Parameter(description = "ID phòng họp", example = "1")
            @PathVariable Long roomId,
            @Parameter(description = "Ngày bắt đầu", example = "2024-09-01")
            @RequestParam String startDate,
            @Parameter(description = "Ngày kết thúc", example = "2024-09-30")
            @RequestParam String endDate) {

        log.info("Getting room calendar (legacy) for roomId: {}, from {} to {}", roomId, startDate, endDate);

        try {
            // Convert string dates to LocalDate
            java.time.LocalDate start = java.time.LocalDate.parse(startDate);
            java.time.LocalDate end = java.time.LocalDate.parse(endDate);

            RoomCalendarRequest request = RoomCalendarRequest.builder()
                .startDate(start)
                .endDate(end)
                .build();

            CalendarOverviewResponse response = adminCalendarService.getRoomCalendar(roomId, request);
            return new ApiResponse<>(200, "Room calendar fetched successfully", response);
        } catch (Exception e) {
            log.error("Error getting room calendar (legacy) for roomId: {}", roomId, e);
            return new ApiResponse<>(400, "Failed to get room calendar: " + e.getMessage(), null);
        }
    }
}
