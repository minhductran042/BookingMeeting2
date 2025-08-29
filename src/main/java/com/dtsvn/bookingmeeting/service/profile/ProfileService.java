package com.dtsvn.bookingmeeting.service.profile;

import com.dtsvn.bookingmeeting.dto.profile.ChangePasswordRequest;
import com.dtsvn.bookingmeeting.dto.profile.ProfileResponse;
import com.dtsvn.bookingmeeting.dto.profile.ProfileUpdateRequest;

public interface ProfileService {
    public ProfileResponse getMyProfile();
    public ProfileResponse updateMyProfile(ProfileUpdateRequest request);
    public ProfileResponse changeMyPassword(ChangePasswordRequest request);
}
