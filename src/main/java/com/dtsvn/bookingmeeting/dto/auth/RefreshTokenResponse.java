package com.dtsvn.bookingmeeting.dto.auth;

import lombok.*;

/**
 * Response DTO for refresh token operation.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
    private String expiresInFormatted;
}
