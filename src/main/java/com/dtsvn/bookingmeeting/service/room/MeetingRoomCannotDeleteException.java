package com.dtsvn.bookingmeeting.service.room;

public class MeetingRoomCannotDeleteException extends RuntimeException {
    
    public MeetingRoomCannotDeleteException(String message) {
        super(message);
    }
    
    public MeetingRoomCannotDeleteException(Long id) {
        super("Cannot delete meeting room with id: " + id + " because it has existing bookings");
    }
}
