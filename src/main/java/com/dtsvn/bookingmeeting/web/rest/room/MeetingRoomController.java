package com.dtsvn.bookingmeeting.web.rest.room;

import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomAvailableRequest;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomCreateRequest;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomResponse;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomUpdateRequest;
import com.dtsvn.bookingmeeting.service.room.MeetingRoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Slf4j
@AllArgsConstructor
public class MeetingRoomController {
    private final MeetingRoomService meetingRoomService;

    // Client Endpoints
    @GetMapping("/active")
    public ApiResponse<List<MeetingRoomResponse>> getActiveRooms() {
        log.info("Getting active meeting rooms");
        try {
            List<MeetingRoomResponse> activeRooms = meetingRoomService.getActiveRooms();
            log.info("Found {} active meeting rooms", activeRooms.size());
            return new ApiResponse<>(200, "Active meeting rooms retrieved successfully", activeRooms);
        } catch (Exception e) {
            log.info("Error getting active meeting rooms: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving active meeting rooms: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<MeetingRoomResponse> getRoomById(@PathVariable Long id) {
        log.info("Getting meeting room by ID: {}", id);
        try {
            MeetingRoomResponse room = meetingRoomService.getRoomById(id);
            return new ApiResponse<>(200, "Meeting room retrieved successfully", room);
        } catch (Exception e) {
            log.error("Error retrieving meeting room by ID: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving meeting room: " + e.getMessage());
        }
    }

    @GetMapping("/available")
    public ApiResponse<List<MeetingRoomResponse>> getAvailableRooms(@RequestBody MeetingRoomAvailableRequest request) {
        log.info("Getting available meeting rooms");
        try {
            List<MeetingRoomResponse> availableRooms = meetingRoomService.getAvailableRooms(request);
            log.info("Found {} available meeting rooms", availableRooms.size());
            return new ApiResponse<>(200, "Available meeting rooms retrieved successfully", availableRooms);
        } catch (Exception e) {
            log.error("Error getting available meeting rooms: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving available meeting rooms: " + e.getMessage());
        }
    }


    // Admin Endpoints
    @GetMapping
    public ApiResponse<List<MeetingRoomResponse>> getAllRooms(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "0") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        log.info("Getting all meeting rooms: page={}, size={}, sortBy={}, sortDirection={}", page, size, sortBy, sortDirection);
        try {
            List<MeetingRoomResponse> rooms = meetingRoomService.getAllRooms(page, size, sortBy, sortDirection);
            return new ApiResponse<>(200, "Meeting rooms retrieved successfully", rooms);
        } catch (Exception e) {
            log.error("Error retrieving meeting rooms: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving meeting rooms: " + e.getMessage());
        }
    }

    @GetMapping("/admin/{id}")
    public ApiResponse<MeetingRoomResponse> getRoomByIdAdmin(@PathVariable Long id) {
        log.info("Getting meeting room by ID for admin: {}", id);
        try {
            MeetingRoomResponse room = meetingRoomService.getRoomByIdForAdmin(id);
            return new ApiResponse<>(200, "Meeting room retrieved successfully", room);
        } catch (Exception e) {
            log.error("Error retrieving meeting room by ID for admin: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving meeting room: " + e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<MeetingRoomResponse> createRoom(@RequestBody MeetingRoomCreateRequest request) {
        log.info("Creating new meeting room: {}", request);
        try {
            MeetingRoomResponse room = meetingRoomService.createRoom(request);
            return new ApiResponse<>(201, "Meeting room created successfully", room);
        } catch (Exception e) {
            log.error("Error creating meeting room: {}", e.getMessage());
            return new ApiResponse<>(500, "Error creating meeting room: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<MeetingRoomResponse> updateRoom(@PathVariable Long id, @RequestBody MeetingRoomUpdateRequest request) {
        log.info("Updating meeting room: id={}, request={}", id, request);
        try {
            MeetingRoomResponse room = meetingRoomService.updateRoom(id, request);
            return new ApiResponse<>(200, "Meeting room updated successfully", room);
        } catch (Exception e) {
            log.error("Error updating meeting room: {}", e.getMessage());
            return new ApiResponse<>(500, "Error updating meeting room: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRoom(@PathVariable Long id) {
        log.info("Deleting meeting room: id={}", id);
        try {
            meetingRoomService.deleteRoom(id);
            return new ApiResponse<>(204, "Meeting room deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting meeting room: {}", e.getMessage());
            return new ApiResponse<>(500, "Error deleting meeting room: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<MeetingRoomResponse> changeRoomStatus(@PathVariable Long id, @RequestParam boolean active) {
        log.info("Changing meeting room status: id={}, active={}", id, active);
        try {
            MeetingRoomResponse room = meetingRoomService.changeRoomStatus(id, active);
            return new ApiResponse<>(200, "Meeting room status changed successfully", room);
        } catch (Exception e) {
            log.error("Error changing meeting room status: {}", e.getMessage());
            return new ApiResponse<>(500, "Error changing meeting room status: " + e.getMessage());
        }
    }
}
