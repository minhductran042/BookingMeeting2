package com.dtsvn.bookingmeeting.dto.user;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for user information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String phone;

    private Role role;

    private String avatarUrl;

    private String department;

    private boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
