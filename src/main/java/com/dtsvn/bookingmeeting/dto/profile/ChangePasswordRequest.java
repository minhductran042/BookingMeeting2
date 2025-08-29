package com.dtsvn.bookingmeeting.dto.profile;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
