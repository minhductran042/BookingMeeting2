package com.dtsvn.bookingmeeting.service.auth;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.auth.LoginRequest;
import com.dtsvn.bookingmeeting.dto.auth.RegisterRequest;
import com.dtsvn.bookingmeeting.dto.auth.LoginResponse;
import com.dtsvn.bookingmeeting.dto.auth.RefreshTokenResponse;
import com.dtsvn.bookingmeeting.dto.auth.RegisterResponse;

public interface AuthenticationService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

    User authenticate(LoginRequest input);
}
