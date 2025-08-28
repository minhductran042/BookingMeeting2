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
    public ApiResponse<BookingResponse> getBookingById(@Parameter(description = "Booking ID") @RequestParam Long id) {
        log.info("Getting booking by ID");
        try {
            BookingResponse booking = bookingAdminService.getBookingById(id);
            return new ApiResponse<>(200, "Booking retrieved successfully", booking);
        } catch (Exception e){
            log.error("Error retrieving booking by ID: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving booking: " + e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Get all bookings", description = "Retrieve all bookings with pagination and sorting")
    public ApiResponse<List<BookingResponse>> getAllBookings(
        @Parameter(description = "Page number (0-based)") @RequestParam int page,
        @Parameter(description = "Page size") @RequestParam int size,
        @Parameter(description = "Sort field") @RequestParam String sortBy,
        @Parameter(description = "Sort direction (asc/desc)") @RequestParam String sortDir) {
        log.info("Getting all bookings");
        try {
            var bookings = bookingAdminService.getAllBookings(page, size, sortBy, sortDir);
            return new ApiResponse<>(200, "Bookings retrieved successfully", bookings);
        } catch (Exception e){
            log.error("Error retrieving all bookings: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving bookings: " + e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Update booking")
    public ApiResponse<BookingResponse> updateBooking(@RequestParam Long id, BookingAdminUpdateRequest request) {
        log.info("Updating booking with ID: {}", id);
        try {
            BookingResponse updatedBooking = bookingAdminService.updateBooking(id, request);
            return new ApiResponse<>(200, "Booking updated successfully", updatedBooking);
        } catch (Exception e){
            log.error("Error updating booking: {}", e.getMessage());
            return new ApiResponse<>(500, "Error updating booking: " + e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete booking")
    public ApiResponse<?> deleteBooking(@RequestParam Long id) {
        log.info("Deleting booking with ID: {}", id);
        try {
            bookingAdminService.deleteBooking(id);
            return new ApiResponse<>(200, "Booking deleted successfully");
        } catch (Exception e){
            log.error("Error deleting booking: {}", e.getMessage());
            return new ApiResponse<>(500, "Error deleting booking: " + e.getMessage());
        }
    }

    @PostMapping("/approve/{id}")
    @Operation(summary = "Approve booking", description = "Approve a pending booking with admin notes")
    public ApiResponse<?> approveBooking(
        @Parameter(description = "Booking ID") @PathVariable Long id,
        @Parameter(description = "Approval request with admin notes") @RequestBody BookingApprovalRequest request) {
        log.info("Approving booking with ID: {}", id);
        try {
            bookingAdminService.approveBooking(id, request);
            return new ApiResponse<>(200, "Booking approved successfully");
        } catch (Exception e){
            log.error("Error approving booking: {}", e.getMessage());
            return new ApiResponse<>(500, "Error approving booking: " + e.getMessage());
        }
    }

    @PostMapping("/reject/{id}")
    @Operation(summary = "Reject booking", description = "Reject a pending booking with rejection reason")
    public ApiResponse<?> rejectBooking(
        @Parameter(description = "Booking ID") @PathVariable Long id,
        @Parameter(description = "Rejection request with reason") @RequestBody BookingApprovalRequest request) {
        log.info("Rejecting booking with ID: {}", id);
        try {
            bookingAdminService.rejectBooking(id, request);
            return new ApiResponse<>(200, "Booking rejected successfully");
        } catch (Exception e){
            log.error("Error rejecting booking: {}", e.getMessage());
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
        @PathVariable String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "0") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        log.info("Getting bookings by status: {}", status);
        try {
            List<BookingResponse> bookings = bookingAdminService.getBookingsByStatus(status, page, size, sortBy, sortDir);
            return new ApiResponse<>(200, "Bookings retrieved successfully", bookings);
        } catch (Exception e) {
            log.error("Error retrieving bookings by status: {}", e.getMessage());
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
    public ApiResponse<Long> getBookingCountByStatus(@PathVariable String status) {
        log.info("Getting booking count by status: {}", status);
        try {
            Long count = bookingAdminService.getBookingsCountByStatus(status);
            return new ApiResponse<>(200, "Booking count by status retrieved successfully", count);
        } catch (Exception e) {
            log.error("Error retrieving booking count by status: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving booking count: " + e.getMessage());
        }
    }

    @GetMapping("statistics/count/meeting-room/{meetingRoomId}")
    @Operation(summary = "Get booking count by meeting room")
    public ApiResponse<Long> getBookingCountByMeetingRoom(@PathVariable Long meetingRoomId) {
        log.info("Getting booking count by meeting room ID: {}", meetingRoomId);
        try {
            Long count = bookingAdminService.getBookingsCountByMeetingRoom(meetingRoomId);
            return new ApiResponse<>(200, "Booking count by meeting room retrieved successfully", count);
        } catch (Exception e) {
            log.error("Error retrieving booking count by meeting room: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving booking count: " + e.getMessage());
        }
    }

    @GetMapping("statistics/count/user/{userId}")
    @Operation(summary = "Get booking count by user")
    public ApiResponse<Long> getBookingCountByUser(@PathVariable Long userId) {
        log.info("Getting booking count by user ID: {}", userId);
        try {
            Long count = bookingAdminService.getBookingsCountByUser(userId);
            return new ApiResponse<>(200, "Booking count by user retrieved successfully", count);
        } catch (Exception e) {
            log.error("Error retrieving booking count by user: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving booking count: " + e.getMessage());
        }
    }
}
