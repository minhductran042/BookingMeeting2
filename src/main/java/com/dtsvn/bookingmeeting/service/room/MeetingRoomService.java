package com.dtsvn.bookingmeeting.service.room;

import com.dtsvn.bookingmeeting.dto.room.MeetingRoomCreateRequest;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomResponse;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomUpdateRequest;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomAvailableRequest;

import java.util.List;

public interface MeetingRoomService {

    // User endpoints
    List<MeetingRoomResponse> getActiveRooms();

    MeetingRoomResponse getRoomById(Long id);

    List<MeetingRoomResponse> getAvailableRooms(MeetingRoomAvailableRequest request);


    // Admin endpoints
    List<MeetingRoomResponse> getAllRooms();
    
    List<MeetingRoomResponse> getAllRooms(int page, int size, String sortBy, String sortDirection);

    MeetingRoomResponse getRoomByIdForAdmin(Long id);

    MeetingRoomResponse createRoom(MeetingRoomCreateRequest request);

    MeetingRoomResponse updateRoom(Long id, MeetingRoomUpdateRequest request);

    void deleteRoom(Long id);

    MeetingRoomResponse changeRoomStatus(Long id, boolean active);
}
