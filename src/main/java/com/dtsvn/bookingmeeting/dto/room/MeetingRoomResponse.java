package com.dtsvn.bookingmeeting.dto.room;

import com.dtsvn.bookingmeeting.dto.location.LocationResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingRoomResponse {

    private Long id;

    private String name;
    private Integer capacity;
    private String description;
    private String equipments;
    private String imageUrl;
    private boolean active;
    private LocationResponse location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
