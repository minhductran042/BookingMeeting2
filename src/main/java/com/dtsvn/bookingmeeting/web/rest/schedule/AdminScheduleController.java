package com.dtsvn.bookingmeeting.web.rest.schedule;

import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.schedule.DailyScheduleResponse;
import com.dtsvn.bookingmeeting.service.schedule.AdminScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST controller for managing admin schedule
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Schedule", description = "APIs for admin schedule management")
public class AdminScheduleController {

    private final AdminScheduleService adminScheduleService;

    @GetMapping("/schedule/daily")
    @Operation(
        summary = "Lấy lịch theo ngày",
        description = "Lấy lịch chi tiết theo ngày với thống kê và xung đột"
    )
    public ResponseEntity<ApiResponse<DailyScheduleResponse>> getDailySchedule(
            @Parameter(description = "Ngày cần xem lịch", example = "2024-01-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            
            @Parameter(description = "ID location (optional)", example = "1")
            @RequestParam(required = false) Long locationId) {
        
        log.info("Getting daily schedule for date: {}, locationId: {}", date, locationId);
        
        try {
            DailyScheduleResponse response = adminScheduleService.getDailySchedule(date, locationId);
            
            return ResponseEntity.ok(ApiResponse.<DailyScheduleResponse>builder()
                .status(200)
                .message("Daily schedule retrieved successfully")
                .data(response)
                .build());
                
        } catch (Exception e) {
            log.error("Error getting daily schedule: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.<DailyScheduleResponse>builder()
                .status(400)
                .message("Error getting daily schedule: " + e.getMessage())
                .build());
        }
    }
}
