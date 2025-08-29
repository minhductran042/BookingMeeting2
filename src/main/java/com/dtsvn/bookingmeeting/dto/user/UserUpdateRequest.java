package com.dtsvn.bookingmeeting.dto.user;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    private String phone;

    private String department;

    private Role role;

    private String avatarUrl;

    private boolean active = true;
}
