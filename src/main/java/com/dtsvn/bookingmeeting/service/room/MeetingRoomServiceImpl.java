package com.dtsvn.bookingmeeting.service.room;

import com.dtsvn.bookingmeeting.domain.room.MeetingRoom;
import com.dtsvn.bookingmeeting.domain.location.Location;
import com.dtsvn.bookingmeeting.dto.room.*;
import com.dtsvn.bookingmeeting.mapper.room.MeetingRoomMapper;
import com.dtsvn.bookingmeeting.repository.room.MeetingRoomRepository;
import com.dtsvn.bookingmeeting.repository.location.LocationRepository;
import com.dtsvn.bookingmeeting.service.room.MeetingRoomNotFoundException;
import com.dtsvn.bookingmeeting.service.room.MeetingRoomCannotDeleteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MeetingRoomServiceImpl implements MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;
    private final LocationRepository locationRepository;
    private final MeetingRoomMapper meetingRoomMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MeetingRoomResponse> getActiveRooms() {
        log.debug("Request to get all active meeting rooms");
        List<MeetingRoom> activeRooms = meetingRoomRepository.findByActive(true);
        return meetingRoomMapper.toResponseList(activeRooms);
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingRoomResponse getRoomById(Long id) {
        log.debug("Request to get meeting room by id: {}", id);
        MeetingRoom meetingRoom = getRoomByIdOrThrow(id);

        if (!meetingRoom.isActive()) {
            throw new RuntimeException("Meeting room is not active");
        }
        return meetingRoomMapper.toResponse(meetingRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingRoomResponse> getAvailableRooms(MeetingRoomAvailableRequest request) {
        log.debug("Request to get available rooms from {} to {}", request.getStartTime(), request.getEndTime());

        List<MeetingRoom> availableRooms;
        if (request.getLocationId() != null) {
            availableRooms = meetingRoomRepository.findAvailableRoomsByLocation(
                request.getStartTime(), request.getEndTime(), request.getLocationId());
        } else {
            availableRooms = meetingRoomRepository.findAvailableRooms(
                request.getStartTime(), request.getEndTime());
        }

        return meetingRoomMapper.toResponseList(availableRooms);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingRoomResponse> getAllRooms() {
        log.debug("Request to get all meeting rooms (admin)");
        List<MeetingRoom> allRooms = meetingRoomRepository.findAll();
        return meetingRoomMapper.toResponseList(allRooms);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingRoomResponse> getAllRooms(int page, int size, String sortBy, String sortDirection) {
        log.debug("Request to get all meeting rooms with pagination: page={}, size={}, sortBy={}, sortDirection={}",
            page, size, sortBy, sortDirection);

        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "id";
        if (sortDirection == null || sortDirection.trim().isEmpty()) sortDirection = "ASC";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        var pageResult = meetingRoomRepository.findAll(pageable);
        List<MeetingRoomResponse> meetingRoomResponses = pageResult.getContent()
            .stream()
            .map(meetingRoomMapper::toResponse)
            .toList();

        return meetingRoomResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingRoomResponse getRoomByIdForAdmin(Long id) {
        log.debug("Request to get meeting room by id for admin: {}", id);
        MeetingRoom meetingRoom = getRoomByIdOrThrow(id);
        return meetingRoomMapper.toResponse(meetingRoom);
    }

    @Override
    public MeetingRoomResponse createRoom(MeetingRoomCreateRequest request) {
        log.debug("Request to create meeting room: {}", request);

        Location location = getLocationByIdOrThrow(request.getLocationId());

        MeetingRoom meetingRoom = meetingRoomMapper.toEntity(request);
        meetingRoom.setLocation(location);

        MeetingRoom savedRoom = meetingRoomRepository.save(meetingRoom);
        return meetingRoomMapper.toResponse(savedRoom);
    }

    @Override
    public MeetingRoomResponse updateRoom(Long id, MeetingRoomUpdateRequest request) {
        log.debug("Request to update meeting room: {}", request);

        MeetingRoom existingRoom = getRoomByIdOrThrow(id);

        if (request.getLocationId() != null) {
            Location location = getLocationByIdOrThrow(request.getLocationId());
            existingRoom.setLocation(location);
        }

        meetingRoomMapper.updateEntity(existingRoom, request);
        MeetingRoom updatedRoom = meetingRoomRepository.save(existingRoom);
        return meetingRoomMapper.toResponse(updatedRoom);
    }

    @Override
    public void deleteRoom(Long id) {
        log.debug("Request to delete meeting room: {}", id);

        MeetingRoom meetingRoom = meetingRoomRepository.findById(id)
            .orElseThrow(() -> new MeetingRoomNotFoundException(id));

        // Check if room has any bookings
        if (!meetingRoom.getBookings().isEmpty()) {
            throw new MeetingRoomCannotDeleteException(id);
        }

        meetingRoomRepository.deleteById(id);
    }

    @Override
    public MeetingRoomResponse changeRoomStatus(Long id, boolean active) {
        MeetingRoom room = getRoomByIdOrThrow(id);
        room.setActive(active);
        MeetingRoom updatedRoom = meetingRoomRepository.save(room);
        return meetingRoomMapper.toResponse(updatedRoom);
    }

    private MeetingRoom getRoomByIdOrThrow(Long id) {
        return meetingRoomRepository.findById(id)
            .orElseThrow(() -> new MeetingRoomNotFoundException(id));
    }

    private Location getLocationByIdOrThrow(Long id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }


}
