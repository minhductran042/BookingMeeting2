package com.dtsvn.bookingmeeting.dto.profile;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private String department;
    private String username;
    @Email(message = "Email format is invalid")
    private String email;
}
