package com.dtsvn.bookingmeeting.service.user.admin;
import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import com.dtsvn.bookingmeeting.dto.user.admin.UserRequest;
import com.dtsvn.bookingmeeting.dto.user.UserResponse;
import com.dtsvn.bookingmeeting.dto.user.admin.UserUpdateRequest;

import java.util.List;

public interface AdminUserService {
    public UserResponse getUserById(Long id);
    public UserResponse createUser(UserRequest request);
    public UserResponse updateUser(Long id, UserUpdateRequest request);
    public void deleteUser(Long id);
    public UserResponse changeUserStatus(Long id, boolean active);
    public List<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDirection);
    public UserResponse changeUserRole(Long id, Role role);
}
