package com.dtsvn.bookingmeeting.web.rest.user.admin;

import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.user.UserResponse;
import com.dtsvn.bookingmeeting.dto.user.admin.ChangeRoleRequest;
import com.dtsvn.bookingmeeting.dto.user.admin.UserRequest;
import com.dtsvn.bookingmeeting.dto.user.admin.UserUpdateRequest;
import com.dtsvn.bookingmeeting.service.user.admin.AdminUserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@AllArgsConstructor
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest request) {
        log.info("Create User: {}", request);
        try {
            UserResponse userResponse = adminUserService.createUser(request);
            return new ApiResponse<>(201, "User created successfully", userResponse);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            return new ApiResponse<>(500, "Error creating user: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        log.info("Update User: {}", request);
        try {
            UserResponse userResponse = adminUserService.updateUser(id, request);
            return new ApiResponse<>(200, "User updated successfully", userResponse);
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return new ApiResponse<>(500, "Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        log.info("Delete User: id={}", id);
        try {
            adminUserService.deleteUser(id);
            return new ApiResponse<>(204, "User deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return new ApiResponse<>(500, "Error deleting user: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        log.info("Get User By Id: {}", id);
        UserResponse userResponse = adminUserService.getUserById(id);
        if (userResponse == null) {
            return new ApiResponse<>(404, "User not found");
        }
        return new ApiResponse<>(200, "User found", userResponse);
    }

    @GetMapping("list")
    public ApiResponse<List<UserResponse>> getUserList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "0") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        log.info("Get User List: page={}, size={}, sortBy={}, sortDirection={}", page, size, sortBy, sortDirection);
        try {
            List<UserResponse> users = adminUserService.getAllUsers(page, size, sortBy, sortDirection);
            return new ApiResponse<>(200, "Users retrieved successfully", users);
        }  catch (Exception e) {
            log.error("Error retrieving user list: {}", e.getMessage());
            return new ApiResponse<>(500, "Error retrieving user list: " + e.getMessage());
        }

    }

    @PostMapping("/{id}/status")
    public ApiResponse<UserResponse> changeUserStatus(@PathVariable Long id, @RequestParam boolean status) {
        log.info("Change User Status: id={}, status={}", id, status);
        UserResponse userResponse = adminUserService.changeUserStatus(id, status);
        if (userResponse == null) {
            return new ApiResponse<>(404, "User not found");
        }
        return new ApiResponse<>(200, "User found", userResponse);
    }

    @PatchMapping("/{id}/role")
    public ApiResponse<UserResponse> changeUserRole(@PathVariable Long id, @RequestBody ChangeRoleRequest request) {
        log.info("Change User Role: id={}, role={}", id, request.getRole());
        try {
            UserResponse userResponse = adminUserService.changeUserRole(id, request.getRole());
            if (userResponse == null) {
                return new ApiResponse<>(404, "User not found");
            }
            return new ApiResponse<>(200, "User role changed successfully", userResponse);
        } catch (Exception e) {
            log.error("Error changing user role: {}", e.getMessage());
            return new ApiResponse<>(500, "Error changing user role: " + e.getMessage());
        }
    }

}
