package com.dtsvn.bookingmeeting.service.user.client;

import com.dtsvn.bookingmeeting.dto.user.UserResponse;
import com.dtsvn.bookingmeeting.dto.user.client.ClientUpdateRequest;
import com.dtsvn.bookingmeeting.dto.user.client.PasswordChangeRequest;

public interface UserService {
    UserResponse myProfile();
    UserResponse updateMyProfile(ClientUpdateRequest request);
    UserResponse changePassword(PasswordChangeRequest request);
}
