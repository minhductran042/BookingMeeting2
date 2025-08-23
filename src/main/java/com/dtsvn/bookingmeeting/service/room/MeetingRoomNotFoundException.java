package com.dtsvn.bookingmeeting.service.room;

public class MeetingRoomNotFoundException extends RuntimeException {
    
    public MeetingRoomNotFoundException(String message) {
        super(message);
    }
    
    public MeetingRoomNotFoundException(Long id) {
        super("Meeting room not found with id: " + id);
    }
}
