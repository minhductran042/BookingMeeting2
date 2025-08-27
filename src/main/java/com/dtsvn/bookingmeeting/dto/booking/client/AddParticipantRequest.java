package com.dtsvn.bookingmeeting.dto.booking.client;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request DTO for adding a participant to a booking.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddParticipantRequest {

    @NotNull(message = "Participant ID is required")
    private Long participantId;
}
