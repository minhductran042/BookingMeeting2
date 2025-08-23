package com.dtsvn.bookingmeeting.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO for user login.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
