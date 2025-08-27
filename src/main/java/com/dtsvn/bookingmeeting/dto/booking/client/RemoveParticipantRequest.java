package com.dtsvn.bookingmeeting.dto.booking.client;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request DTO for removing a participant from a booking.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemoveParticipantRequest {

    @NotNull(message = "Participant ID is required")
    private Long participantId;
}
