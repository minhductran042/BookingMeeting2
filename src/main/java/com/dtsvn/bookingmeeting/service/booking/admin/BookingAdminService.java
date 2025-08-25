package com.dtsvn.bookingmeeting.service.booking.admin;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingApprovalRequest;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminSearchRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingAdminService {
    
    // Basic CRUD operations
    BookingResponse getBookingById(Long id);
    List<BookingResponse> getAllBookings(int page, int size, String sortBy, String sortDir);
    BookingResponse updateBooking(Long id, BookingAdminUpdateRequest request);
    void deleteBooking(Long id);
    
    // Approval operations
    void approveBooking(Long id, BookingApprovalRequest request);
    void rejectBooking(Long id, BookingApprovalRequest request);
    
    // Search and filter operations
    List<BookingResponse> searchBookings(BookingAdminSearchRequest searchRequest, int page, int size, String sortBy, String sortDir);
    List<BookingResponse> getBookingsByStatus(String status, int page, int size, String sortBy, String sortDir);

    // Statistics and reporting
    long getTotalBookingsCount();
    long getBookingsCountByStatus(String status);
    long getBookingsCountByMeetingRoom(Long meetingRoomId);
    long getBookingsCountByUser(Long userId);
}
