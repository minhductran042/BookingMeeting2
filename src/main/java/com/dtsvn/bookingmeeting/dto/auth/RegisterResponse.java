package com.dtsvn.bookingmeeting.dto.auth;

import lombok.*;
import com.dtsvn.bookingmeeting.dto.auth.UserInfo;

/**
 * Response DTO for user registration.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String message;
    private UserInfo user;
}
