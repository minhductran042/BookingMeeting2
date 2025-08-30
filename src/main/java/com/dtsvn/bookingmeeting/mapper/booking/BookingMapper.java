package com.dtsvn.bookingmeeting.mapper.booking;
import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.booking.BookingParticipant;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.BookingCreateRequest;
import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.dto.user.UserResponse;
import com.dtsvn.bookingmeeting.mapper.user.UserMapper;
import com.dtsvn.bookingmeeting.mapper.room.MeetingRoomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

/**
 * Mapper for converting between Booking entity and DTOs.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, MeetingRoomMapper.class})
public interface BookingMapper {
    Booking toEntity(BookingCreateRequest request);

    @Mapping(source = "participants", target = "participants", qualifiedByName = "participantsToUserResponses")
    BookingResponse toResponse(Booking booking);

    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    Booking updateEntity(@MappingTarget Booking booking, BookingAdminUpdateRequest request);

    List<BookingResponse> toResponseList(List<Booking> bookings);
    
    @Named("participantsToUserResponses")
    default List<UserResponse> participantsToUserResponses(Set<BookingParticipant> participants) {
        if (participants == null) {
            return null;
        }
        return participants.stream()
                .map(BookingParticipant::getUser)
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .phone(user.getPhone())
                        .role(user.getRole())
                        .avatarUrl(user.getAvatarUrl())
                        .department(user.getDepartment())
                        .active(user.isActive())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build())
                .toList();
    }
}
