package com.dtsvn.bookingmeeting.dto.room;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoomUpdateRequest {

    @Size(min = 1, max = 100)
    private String name;

    private Long locationId;

    private Integer capacity;

    private String description;

    private String equipments;

    private String imageUrl;

    private Boolean active;
}
