package com.dtsvn.bookingmeeting.dto.user.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
