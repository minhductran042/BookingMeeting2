package com.dtsvn.bookingmeeting.web.rest.auth;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.service.auth.AuthenticationService;
import com.dtsvn.bookingmeeting.dto.auth.LoginRequest;
import com.dtsvn.bookingmeeting.dto.auth.RegisterRequest;
import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.auth.LoginResponse;
import com.dtsvn.bookingmeeting.dto.auth.RefreshTokenResponse;
import com.dtsvn.bookingmeeting.dto.auth.RegisterResponse;
import com.dtsvn.bookingmeeting.dto.auth.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs for user authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("register")
    @Operation(summary = "User registration")
    public ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        try {
            RegisterResponse response = authenticationService.register(request);
            return new ApiResponse<>(HttpStatus.CREATED.value(), "Registration successful", response);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("login")
    @Operation(summary = "User login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authenticationService.login(request);
            
            // Set refresh token vào httpOnly cookie
            Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false); // Set false cho development, true cho production HTTPS
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            response.addCookie(refreshTokenCookie);
            
            // Không trả về refresh token trong response body
            LoginResponse responseWithoutRefreshToken = LoginResponse.builder()
                    .accessToken(loginResponse.getAccessToken())
                    .tokenType(loginResponse.getTokenType())
                    .expiresIn(loginResponse.getExpiresIn())
                    .expiresInFormatted(loginResponse.getExpiresInFormatted())
                    .userInfo(loginResponse.getUserInfo())
                    .build();
            
            return new ApiResponse<>(HttpStatus.OK.value(), "Login successful", responseWithoutRefreshToken);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    @PostMapping("refresh-token")
    @Operation(summary = "Refresh token")
    public ApiResponse<RefreshTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Lấy refresh token từ cookie thay vì request body
            String refreshToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }
            
            if (refreshToken == null) {
                return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Refresh token not found");
            }
            
            RefreshTokenResponse refreshResponse = authenticationService.refreshToken(refreshToken);
            
            // Set refresh token mới vào cookie
            Cookie newRefreshTokenCookie = new Cookie("refreshToken", refreshResponse.getRefreshToken());
            newRefreshTokenCookie.setHttpOnly(true);
            newRefreshTokenCookie.setSecure(false); // Set false cho development, true cho production HTTPS
            newRefreshTokenCookie.setPath("/");
            newRefreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            response.addCookie(newRefreshTokenCookie);
            
            // Không trả về refresh token trong response body
            RefreshTokenResponse responseWithoutRefreshToken = RefreshTokenResponse.builder()
                    .accessToken(refreshResponse.getAccessToken())
                    .tokenType(refreshResponse.getTokenType())
                    .expiresIn(refreshResponse.getExpiresIn())
                    .build();
            
            return new ApiResponse<>(HttpStatus.OK.value(), "Token refreshed successfully", responseWithoutRefreshToken);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("logout")
    @Operation(summary = "User logout")
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Lấy refresh token từ cookie
            String refreshToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }
            
            if (refreshToken != null) {
                authenticationService.logout(refreshToken);
            }
            
            // Xóa refresh token cookie
            Cookie deleteCookie = new Cookie("refreshToken", "");
            deleteCookie.setHttpOnly(true);
            deleteCookie.setSecure(false); // Set false cho development, true cho production HTTPS
            deleteCookie.setPath("/");
            deleteCookie.setMaxAge(0); // Xóa cookie ngay lập tức
            response.addCookie(deleteCookie);
            
            return new ApiResponse<>(HttpStatus.OK.value(), "Logout successful");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("me")
    @Operation(summary = "Get current user info")
    public ApiResponse<UserInfo> getCurrentUser(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            UserInfo userInfo = UserInfo.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole())
                    .isActive(user.isActive())
                    .authorities(user.getAuthorities())
                    .build();
            return new ApiResponse<>(HttpStatus.OK.value(), "User info retrieved successfully", userInfo);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
