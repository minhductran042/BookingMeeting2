package com.dtsvn.bookingmeeting.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NotificationResponse {
    private String title;
    private String body;
    private String tokenDevice;
    private String imageUrl;
    private LocalDateTime sentAt;
    private Map<String,String> data;
    private boolean isSeen;
    private String message;
}
