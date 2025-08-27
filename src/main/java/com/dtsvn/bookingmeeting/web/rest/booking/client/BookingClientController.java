package com.dtsvn.bookingmeeting.web.rest.booking.client;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.dto.booking.client.AddParticipantRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.BookingCreateRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.BookingUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.RemoveParticipantRequest;
import com.dtsvn.bookingmeeting.service.booking.client.BookingClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/booking")
@Tag(name = "Client Booking Management", description = "APIs for managing bookings by clients")
@Slf4j(topic = "ClIENT_BOOKING_CONTROLLER")
@RequiredArgsConstructor
public class BookingClientController {
    private final BookingClientService bookingClientService;

    @GetMapping("/my")
    public ApiResponse<List<BookingResponse>> getMyBookings() {
        log.info("Fetching bookings for the authenticated client");
        try {
            List<BookingResponse> bookings = bookingClientService.getMyBookings();
            return ApiResponse.<List<BookingResponse>>builder()
                .status(200)
                .message("Bookings fetched successfully")
                .data(bookings)
                .build();
        } catch (Exception e) {
            log.error("Error fetching bookings: {}", e.getMessage());
            return ApiResponse.<List<BookingResponse>>builder()
                .status(500)
                .message("Error fetching bookings")
                .data(null)
                .build();
        }
    }

    @GetMapping("/participated")
    public ApiResponse<List<BookingResponse>> getParticipatedBookings() {
        log.info("Fetching bookings where the authenticated client is a participant");
        try {
            List<BookingResponse> bookings = bookingClientService.getParticipatedBookings();
            return ApiResponse.<List<BookingResponse>>builder()
                .status(200)
                .message("Participated bookings fetched successfully")
                .data(bookings)
                .build();
        } catch (Exception e) {
            log.error("Error fetching participated bookings: {}", e.getMessage());
            return ApiResponse.<List<BookingResponse>>builder()
                .status(500)
                .message("Error fetching participated bookings")
                .data(null)
                .build();
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> getBookingById(@PathVariable Long id) {
        log.info("Fetching booking with ID: {}", id);
        try {
            BookingResponse booking = bookingClientService.getBookingById(id);
            return ApiResponse.<BookingResponse>builder()
                .status(200)
                .message("Booking fetched successfully")
                .data(booking)
                .build();
        } catch (Exception e) {
            log.error("Error fetching booking with ID {}: {}", id, e.getMessage());
            return ApiResponse.<BookingResponse>builder()
                .status(500)
                .message("Error fetching booking")
                .data(null)
                .build();
        }
    }

    @PostMapping()
    public ApiResponse<BookingResponse> createBooking(@RequestBody BookingCreateRequest request) {
        log.info("Creating a new booking");
        try {
            BookingResponse booking = bookingClientService.createBooking(request);
            return ApiResponse.<BookingResponse>builder()
                .status(201)
                .message("Booking created successfully")
                .data(booking)
                .build();
        } catch (Exception e) {
            log.error("Error creating booking: {}", e.getMessage());
            return ApiResponse.<BookingResponse>builder()
                .status(500)
                .message("Error creating booking")
                .data(null)
                .build();
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<BookingResponse> updateBooking(@PathVariable Long id, @RequestBody BookingUpdateRequest request) {
        log.info("Updating booking with ID: {}", id);
        try {
            BookingResponse booking = bookingClientService.updateBooking(id, request);
            return ApiResponse.<BookingResponse>builder()
                .status(200)
                .message("Booking updated successfully")
                .data(booking)
                .build();
        } catch (Exception e) {
            log.error("Error updating booking with ID {}: {}", id, e.getMessage());
            return ApiResponse.<BookingResponse>builder()
                .status(500)
                .message("Error updating booking")
                .data(null)
                .build();
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> cancellBooking(@PathVariable Long id) {
        log.info("Deleting booking with ID: {}", id);
        try {
            bookingClientService.cancelledBooking(id);
            return ApiResponse.<Void>builder()
                .status(204)
                .message("Booking deleted successfully")
                .data(null)
                .build();
        } catch (Exception e) {
            log.error("Error deleting booking with ID {}: {}", id, e.getMessage());
            return ApiResponse.<Void>builder()
                .status(500)
                .message("Error deleting booking")
                .data(null)
                .build();
        }
    }

    @PostMapping("add-participant/{bookingId}")
    public ApiResponse<Void> addParticipant(@PathVariable Long bookingId, @RequestBody AddParticipantRequest request) {
        log.info("Adding participant with id {} to booking ID: {}", request.getParticipantId(), bookingId);
        try {
            bookingClientService.addParticipant(bookingId, request);
            return ApiResponse.<Void>builder()
                .status(200)
                .message("Participant added successfully")
                .data(null)
                .build();
        } catch (Exception e) {
            log.error("Error adding participant to booking ID {}: {}", bookingId, e.getMessage());
            return ApiResponse.<Void>builder()
                .status(500)
                .message("Error adding participant")
                .data(null)
                .build();
        }
    }

    @PostMapping("remove-participant/{bookingId}")
    public ApiResponse<Void> removeParticipant(@PathVariable Long bookingId, @RequestBody RemoveParticipantRequest request) {
        log.info("Removing participant with id {} from booking ID: {}", request.getParticipantId(), bookingId);
        try {
            bookingClientService.removeParticipant(bookingId, request);
            return ApiResponse.<Void>builder()
                .status(200)
                .message("Participant removed successfully")
                .data(null)
                .build();
        } catch (Exception e) {
            log.error("Error removing participant from booking ID {}: {}", bookingId, e.getMessage());
            return ApiResponse.<Void>builder()
                .status(500)
                .message("Error removing participant")
                .data(null)
                .build();
        }
    }
}
