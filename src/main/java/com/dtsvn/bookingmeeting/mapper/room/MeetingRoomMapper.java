package com.dtsvn.bookingmeeting.mapper.room;

import com.dtsvn.bookingmeeting.domain.room.MeetingRoom;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomCreateRequest;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomUpdateRequest;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomResponse;
import com.dtsvn.bookingmeeting.mapper.location.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface MeetingRoomMapper {

    MeetingRoom toEntity(MeetingRoomCreateRequest request);
    void updateEntity(@MappingTarget MeetingRoom meetingRoom, MeetingRoomUpdateRequest request);
    List<MeetingRoomResponse> toResponseList(List<MeetingRoom> meetingRooms);
    MeetingRoomResponse toResponse(MeetingRoom meetingRoom);
}
