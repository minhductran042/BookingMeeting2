package com.dtsvn.bookingmeeting.web.rest.auth;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.service.auth.AuthenticationService;
import com.dtsvn.bookingmeeting.dto.auth.LoginRequest;
import com.dtsvn.bookingmeeting.dto.auth.RegisterRequest;
import com.dtsvn.bookingmeeting.dto.auth.RefreshTokenRequest;
import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.auth.LoginResponse;
import com.dtsvn.bookingmeeting.dto.auth.RefreshTokenResponse;
import com.dtsvn.bookingmeeting.dto.auth.RegisterResponse;
import com.dtsvn.bookingmeeting.dto.auth.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("register")
    public ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        try {
            RegisterResponse response = authenticationService.register(request);
            return new ApiResponse<>(HttpStatus.CREATED.value(), "Registration successful", response);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        try {
            LoginResponse response = authenticationService.login(request);
            return new ApiResponse<>(HttpStatus.OK.value(), "Login successful", response);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    @PostMapping("refresh-token")
    public ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            RefreshTokenResponse response = authenticationService.refreshToken(request.getRefreshToken());
            return new ApiResponse<>(HttpStatus.OK.value(), "Token refreshed successfully", response);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("logout")
    public ApiResponse<?> logout(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            authenticationService.logout(request.getRefreshToken());
            return new ApiResponse<>(HttpStatus.OK.value(), "Logout successful");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("me")
    public ApiResponse<UserInfo> getCurrentUser(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            UserInfo userInfo = UserInfo.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole().name())
                    .isActive(user.isActive())
                    .authorities(user.getAuthorities())
                    .build();
            return new ApiResponse<>(HttpStatus.OK.value(), "User info retrieved successfully", userInfo);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
