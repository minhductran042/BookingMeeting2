package com.dtsvn.bookingmeeting.dto.booking.admin;

import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Admin request DTO for searching and filtering bookings.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingAdminSearchRequest {

    private String title;
    
    private String createdByUsername;
    
    private Long meetingRoomId;
    
    private BookingStatus status;
    
    private LocalDateTime startTimeFrom;
    
    private LocalDateTime startTimeTo;
    
    private LocalDateTime endTimeFrom;
    
    private LocalDateTime endTimeTo;
    
    private LocalDateTime createdAtFrom;
    
    private LocalDateTime createdAtTo;
    
    private String department;
}
