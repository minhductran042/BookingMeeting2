package com.dtsvn.bookingmeeting.dto.profile;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Role role;
    private String department;
}
