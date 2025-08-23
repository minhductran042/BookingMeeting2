package com.dtsvn.bookingmeeting.dto.auth;

import lombok.*;
import com.dtsvn.bookingmeeting.dto.auth.UserInfo;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
    private String expiresInFormatted;
    private UserInfo userInfo;
}
