package com.dtsvn.bookingmeeting.dto.user;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for user information response.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private Role role;
    private String avatarUrl;
    private String department;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
