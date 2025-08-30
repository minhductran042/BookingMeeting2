package com.dtsvn.bookingmeeting.web.rest.booking.admin;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminSearchRequest;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingApprovalRequest;
import com.dtsvn.bookingmeeting.service.booking.admin.BookingAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/booking")
@Tag(name = "Admin Booking Management", description = "APIs for managing bookings by administrators")
@Slf4j(topic = "ADMIN_BOOKING_CONTROLLER")
@RequiredArgsConstructor
public class AdminBookingController {
    private final BookingAdminService bookingAdminService;

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID", description = "Retrieve a specific booking by its ID")
    public ApiResponse<BookingResponse> getBookingById(
            @Parameter(description = "Booking ID", required = true)
            @PathVariable Long id) {

        // Validate path variable
        if (id == null || id <= 0) {
            log.warn("Invalid booking ID provided: {}", id);
            return new ApiResponse<>(400, "Invalid booking ID");
        }

        log.info("Getting booking by ID: {}", id);
        try {
            BookingResponse booking = bookingAdminService.getBookingById(id);
            return new ApiResponse<>(200, "Booking retrieved successfully", booking);
        } catch (IllegalArgumentException e) {
            log.warn("Booking not found with ID: {}", id);
            return new ApiResponse<>(404, "Booking not found: " + e.getMessage());
        } catch (Exception e){
            log.error("Error retrieving booking by ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse<>(500, "Error retrieving booking: " + e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Get all bookings", description = "Retrieve all bookings with pagination and sorting")
    public ApiResponse<List<BookingResponse>> getAllBookings(
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "0") int size,
        @RequestParam(required = false, defaultValue = "id") String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        log.info("Getting all bookings");
        try {
            var bookings = bookingAdminService.getAllBookings(page, size, sortBy, sortDirection);
            return new ApiResponse<>(200, "Bookings retrieved successfully", bookings);
        } catch (Exception e){
            log.error("Error retrieving all bookings: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving bookings: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update booking")
    public ApiResponse<BookingResponse> updateBooking(
            @Parameter(description = "Booking ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid BookingAdminUpdateRequest request) {

        // Validate path variable
        if (id == null || id <= 0) {
            log.warn("Invalid booking ID provided: {}", id);
            return new ApiResponse<>(400, "Invalid booking ID");
        }

        log.info("Updating booking with ID: {}", id);
        try {
            BookingResponse updatedBooking = bookingAdminService.updateBooking(id, request);
            return new ApiResponse<>(200, "Booking updated successfully", updatedBooking);
        } catch (Exception e){
            log.error("Error updating booking with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse<>(500, "Error updating booking: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete booking")
    public ApiResponse<?> deleteBooking(
            @Parameter(description = "Booking ID", required = true)
            @PathVariable Long id) {

        // Validate path variable
        if (id == null || id <= 0) {
            log.warn("Invalid booking ID provided for deletion: {}", id);
            return new ApiResponse<>(400, "Invalid booking ID");
        }

        log.info("Deleting booking with ID: {}", id);
        try {
            bookingAdminService.deleteBooking(id);
            return new ApiResponse<>(200, "Booking deleted successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Invalid booking ID for deletion: {}", e.getMessage());
            return new ApiResponse<>(400, "Invalid booking ID: " + e.getMessage());
        } catch (Exception e){
            log.error("Error deleting booking with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse<>(500, "Error deleting booking: " + e.getMessage());
        }
    }

    @PostMapping("/approve/{id}")
    @Operation(summary = "Approve booking", description = "Approve a pending booking with admin notes")
    public ApiResponse<?> approveBooking(
        @Parameter(description = "Booking ID", required = true) @PathVariable Long id,
        @Parameter(description = "Approval request with admin notes") @RequestBody BookingApprovalRequest request) {

        // Validate path variable
        if (id == null || id <= 0) {
            log.warn("Invalid booking ID provided for approval: {}", id);
            return new ApiResponse<>(400, "Invalid booking ID");
        }

        log.info("Approving booking with ID: {}", id);
        try {
            bookingAdminService.approveBooking(id, request);
            return new ApiResponse<>(200, "Booking approved successfully");
        } catch (IllegalStateException e) {
            log.warn("Cannot approve booking: {}", e.getMessage());
            return new ApiResponse<>(400, "Cannot approve booking: " + e.getMessage());
        } catch (Exception e){
            log.error("Error approving booking with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse<>(500, "Error approving booking: " + e.getMessage());
        }
    }

    @PostMapping("/reject/{id}")
    @Operation(summary = "Reject booking", description = "Reject a pending booking with rejection reason")
    public ApiResponse<?> rejectBooking(
        @Parameter(description = "Booking ID", required = true) @PathVariable Long id,
        @Parameter(description = "Rejection request with reason") @RequestBody BookingApprovalRequest request) {

        // Validate path variable
        if (id == null || id <= 0) {
            log.warn("Invalid booking ID provided for rejection: {}", id);
            return new ApiResponse<>(400, "Invalid booking ID");
        }

        log.info("Rejecting booking with ID: {}", id);
        try {
            bookingAdminService.rejectBooking(id, request);
            return new ApiResponse<>(200, "Booking rejected successfully");
        } catch (IllegalStateException e) {
            log.warn("Cannot reject booking: {}", e.getMessage());
            return new ApiResponse<>(400, "Cannot reject booking: " + e.getMessage());
        } catch (Exception e){
            log.error("Error rejecting booking with ID {}: {}", id, e.getMessage(), e);
            return new ApiResponse<>(500, "Error rejecting booking: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    @Operation(summary = "Search bookings")
    public ApiResponse<List<BookingResponse>> searchBookings(
        @Valid @RequestBody BookingAdminSearchRequest request,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "0") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDir
        ) {
        log.info("Searching bookings with criteria: {}", request);
        try {
            List<BookingResponse> bookings = bookingAdminService.searchBookings(request, page, size, sortBy, sortDir);
            return new ApiResponse<>(200, "Bookings retrieved successfully", bookings);
        } catch (Exception e) {
            log.error("Error searching bookings: {}", e.getMessage());
            return new ApiResponse<>(500, "Error searching bookings: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get bookings by status")
    public ApiResponse<List<BookingResponse>> getBookingsByStatus(
        @Parameter(description = "Booking status", required = true)
        @PathVariable String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "0") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        // Validate path variable
        if (status == null || status.trim().isEmpty()) {
            log.warn("Invalid status provided: {}", status);
            return new ApiResponse<>(400, "Invalid status parameter");
        }

        log.info("Getting bookings by status: {}", status);
        try {
            List<BookingResponse> bookings = bookingAdminService.getBookingsByStatus(status, page, size, sortBy, sortDir);
            return new ApiResponse<>(200, "Bookings retrieved successfully", bookings);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid status value: {}", e.getMessage());
            return new ApiResponse<>(400, "Invalid status: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving bookings by status {}: {}", status, e.getMessage(), e);
            return new ApiResponse<>(500, "Error retrieving bookings: " + e.getMessage());
        }
    }

    @GetMapping("statistics/count")
    @Operation(summary = "Get total booking count")
    public ApiResponse<Long> getTotalBookingCount() {
        log.info("Getting total booking count");
        try {
            Long count = bookingAdminService.getTotalBookingsCount();
            return new ApiResponse<>(200, "Total booking count retrieved successfully", count);
        } catch (Exception e) {
            log.error("Error retrieving total booking count: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving booking count: " + e.getMessage());
        }
    }

    @GetMapping("statistics/count/status/{status}")
    @Operation(summary = "Get booking count by status")
    public ApiResponse<Long> getBookingCountByStatus(
            @Parameter(description = "Booking status", required = true)
            @PathVariable String status) {

        // Validate path variable
        if (status == null || status.trim().isEmpty()) {
            log.warn("Invalid status provided for count: {}", status);
            return new ApiResponse<>(400, "Invalid status parameter");
        }

        log.info("Getting booking count by status: {}", status);
        try {
            Long count = bookingAdminService.getBookingsCountByStatus(status);
            return new ApiResponse<>(200, "Booking count by status retrieved successfully", count);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid status value for count: {}", e.getMessage());
            return new ApiResponse<>(400, "Invalid status: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving booking count by status {}: {}", status, e.getMessage(), e);
            return new ApiResponse<>(500, "Error retrieving booking count: " + e.getMessage());
        }
    }

    @GetMapping("statistics/count/meeting-room/{meetingRoomId}")
    @Operation(summary = "Get booking count by meeting room")
    public ApiResponse<Long> getBookingCountByMeetingRoom(
            @Parameter(description = "Meeting room ID", required = true)
            @PathVariable Long meetingRoomId) {

        // Validate path variable
        if (meetingRoomId == null || meetingRoomId <= 0) {
            log.warn("Invalid meeting room ID provided: {}", meetingRoomId);
            return new ApiResponse<>(400, "Invalid meeting room ID");
        }

        log.info("Getting booking count by meeting room ID: {}", meetingRoomId);
        try {
            Long count = bookingAdminService.getBookingsCountByMeetingRoom(meetingRoomId);
            return new ApiResponse<>(200, "Booking count by meeting room retrieved successfully", count);
        } catch (IllegalArgumentException e) {
            log.warn("Meeting room not found with ID: {}", meetingRoomId);
            return new ApiResponse<>(404, "Meeting room not found: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving booking count by meeting room {}: {}", meetingRoomId, e.getMessage(), e);
            return new ApiResponse<>(500, "Error retrieving booking count: " + e.getMessage());
        }
    }

    @GetMapping("statistics/count/user/{userId}")
    @Operation(summary = "Get booking count by user")
    public ApiResponse<Long> getBookingCountByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {

        // Validate path variable
        if (userId == null || userId <= 0) {
            log.warn("Invalid user ID provided: {}", userId);
            return new ApiResponse<>(400, "Invalid user ID");
        }

        log.info("Getting booking count by user ID: {}", userId);
        try {
            Long count = bookingAdminService.getBookingsCountByUser(userId);
            return new ApiResponse<>(200, "Booking count by user retrieved successfully", count);
        } catch (IllegalArgumentException e) {
            log.warn("User not found with ID: {}", userId);
            return new ApiResponse<>(404, "User not found: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving booking count by user {}: {}", userId, e.getMessage(), e);
            return new ApiResponse<>(500, "Error retrieving booking count: " + e.getMessage());
        }
    }
}
