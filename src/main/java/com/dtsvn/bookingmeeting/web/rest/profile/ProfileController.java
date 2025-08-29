package com.dtsvn.bookingmeeting.web.rest.profile;

import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.profile.ChangePasswordRequest;
import com.dtsvn.bookingmeeting.dto.profile.ProfileResponse;
import com.dtsvn.bookingmeeting.dto.profile.ProfileUpdateRequest;
import com.dtsvn.bookingmeeting.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/my")
    public ApiResponse<ProfileResponse> getMyProfile() {
        log.info("Request to get my profile");
        try {
            ProfileResponse profile = profileService.getMyProfile();
            return new ApiResponse<>(200, "Successfully retrieved profile", profile);
        } catch (Exception e) {
            log.error("Error retrieving profile: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving profile: " + e.getMessage(), null);
        }
    }

    @PutMapping
    public ApiResponse<ProfileResponse> updateMyProfile(@RequestBody @Valid ProfileUpdateRequest request) {
        log.info("Request to update my profile: {}", request);
        try {
            ProfileResponse updatedProfile = profileService.updateMyProfile(request);
            return new ApiResponse<>(200, "Successfully updated profile", updatedProfile);
        } catch (Exception e) {
            log.error("Error updating profile: {}", e.getMessage());
            return new ApiResponse<>(500, "Error updating profile: " + e.getMessage(), null);
        }
    }

    @PatchMapping("/change-password")
    public ApiResponse<ProfileResponse> changeMyPassword(@RequestBody ChangePasswordRequest request) {
        log.info("Request to change my password");
        try {
            ProfileResponse updatedProfile = profileService.changeMyPassword(request);
            return new ApiResponse<>(200, "Successfully changed password", updatedProfile);
        } catch (Exception e) {
            log.error("Error changing password: {}", e.getMessage());
            return new ApiResponse<>(500, "Error changing password: " + e.getMessage(), null);
        }
    }
}
