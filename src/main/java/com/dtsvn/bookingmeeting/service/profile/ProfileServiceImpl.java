package com.dtsvn.bookingmeeting.service.profile;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.profile.ChangePasswordRequest;
import com.dtsvn.bookingmeeting.dto.profile.ProfileResponse;
import com.dtsvn.bookingmeeting.dto.profile.ProfileUpdateRequest;
import com.dtsvn.bookingmeeting.dto.user.UserResponse;
import com.dtsvn.bookingmeeting.repository.user.UserRepository;
import com.dtsvn.bookingmeeting.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileResponse getMyProfile() {
        log.info("Getting my profile");
        try {
            User currentUser = securityUtils.getCurrentAuthenticatedUser();
            return ProfileResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .phoneNumber(currentUser.getPhone())
                .role(currentUser.getRole())
                .department(currentUser.getDepartment())
                .build();
        } catch (Exception e) {
            log.error("Error getting my profile: {}", e.getMessage());
            throw new RuntimeException("Error getting my profile: " + e.getMessage());
        }
    }

    @Override
    public ProfileResponse updateMyProfile(ProfileUpdateRequest request) {
        log.info("Updating my profile with request: {}", request);
        try {
            User currentUser = securityUtils.getCurrentAuthenticatedUser();
            if (request.getFullName() != null) {
                currentUser.setFullName(request.getFullName());
            }
            if (request.getPhoneNumber() != null) {
                currentUser.setPhone(request.getPhoneNumber());
            }
            if (request.getDepartment() != null) {
                currentUser.setDepartment(request.getDepartment());
            }
            if (request.getUsername() != null) {
                currentUser.setUsername(request.getUsername());
            }
            if (request.getEmail() != null) {
                currentUser.setEmail(request.getEmail());
            }
            userRepository.save(currentUser);
            return ProfileResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .phoneNumber(currentUser.getPhone())
                .role(currentUser.getRole())
                .department(currentUser.getDepartment())
                .build();
        } catch (Exception e) {
            log.error("Error updating my profile: {}", e.getMessage());
            throw new RuntimeException("Error updating my profile: " + e.getMessage());
        }
    }

    @Override
    public ProfileResponse changeMyPassword(ChangePasswordRequest request) {
        log.info("Changing my password");
        try {
            User currentUser = securityUtils.getCurrentAuthenticatedUser();
            if(!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
                throw new RuntimeException("Old password is incorrect");
            }

            if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new RuntimeException("New password and confirm new password do not match");
            }

            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(currentUser);
            return ProfileResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .phoneNumber(currentUser.getPhone())
                .role(currentUser.getRole())
                .department(currentUser.getDepartment())
                .build();
        } catch (Exception e) {
            log.error("Error changing my password: {}", e.getMessage());
            throw new RuntimeException("Error changing my password: " + e.getMessage());
        }
    }
}
