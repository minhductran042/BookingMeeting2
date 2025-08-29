package com.dtsvn.bookingmeeting.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO để gửi thông báo cho user cụ thể
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendToUserRequest {
    
    @NotBlank(message = "User email is required")
    private String userEmail;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Body is required")
    private String body;
    
    private String imageUrl;
    
    private Map<String, String> data = new HashMap<>();
    
    public SendToUserRequest addData(String key, String value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }
}

