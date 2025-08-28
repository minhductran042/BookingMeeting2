package com.dtsvn.bookingmeeting.service.booking.client;

import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.dto.booking.client.BookingCreateRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.BookingUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.AddParticipantRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.RemoveParticipantRequest;

import java.util.List;

public interface BookingClientService {

    List<BookingResponse> getMyBookings();

    List<BookingResponse> getParticipatedBookings();

    List<BookingResponse> getMyApprovedBookings();

    List<BookingResponse> getParticipatedApprovedBookings();

    List<BookingResponse> getMyPendingBookings();

    List<BookingResponse> getParticipatedPendingBookings();

    BookingResponse getBookingById(Long id);

    BookingResponse createBooking(BookingCreateRequest request);

    BookingResponse updateBooking(Long id, BookingUpdateRequest request);

    void addParticipant(Long bookingId, AddParticipantRequest request);

    void removeParticipant(Long bookingId, RemoveParticipantRequest request);

    void cancelledBooking(Long id);
}
