package com.dtsvn.bookingmeeting.dto.booking.admin;

import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Admin request DTO for booking approval/rejection.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingApprovalRequest {

    @Size(max = 500)
    private String adminNotes;

    @Size(max = 200)
    private String reason;
}
