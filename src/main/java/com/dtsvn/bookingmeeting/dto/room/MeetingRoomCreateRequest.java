package com.dtsvn.bookingmeeting.dto.room;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoomCreateRequest {

    @NotNull
    private String name;

    @NotNull
    private Long locationId;

    private Integer capacity;

    private String description;

    private String equipments;

    private String imageUrl;

    @Builder.Default
    private boolean active = true;
}
