package com.dtsvn.bookingmeeting.mapper.booking;
import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.BookingCreateRequest;
import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.mapper.user.UserMapper;
import com.dtsvn.bookingmeeting.mapper.room.MeetingRoomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper for converting between Booking entity and DTOs.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, MeetingRoomMapper.class})
public interface BookingMapper {
    Booking toEntity(BookingCreateRequest request);

    BookingResponse toResponse(Booking booking);

    Booking updateEntity(@MappingTarget Booking booking, BookingAdminUpdateRequest request);

    List<BookingResponse> toResponseList(List<Booking> bookings);
}
