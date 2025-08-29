package com.dtsvn.bookingmeeting.dto.user;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request DTO for creating/updating a user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String phone;

    private String department;

    @NotBlank(message = "Role is required")
    private Role role;

    private String avatarUrl;

    private boolean active = true;
}
