package com.dtsvn.bookingmeeting.dto.booking.admin;

import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Admin request DTO for updating booking information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingAdminUpdateRequest {

    @Size(max = 200)
    private String title;

    @Size(max = 500)
    private String description;

    @Size(max = 500)
    private String purpose;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotNull
    private BookingStatus status;

    private String adminNotes;
}
