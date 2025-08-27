package com.dtsvn.bookingmeeting.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để frontend đăng ký device token cho web browser
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDeviceRequest {

    @NotBlank(message = "Device token is required")
    private String deviceToken;

    private String deviceInfo; // Browser info, OS, etc.
}
