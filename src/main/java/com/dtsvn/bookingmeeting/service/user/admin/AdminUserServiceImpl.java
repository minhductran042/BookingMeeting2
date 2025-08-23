package com.dtsvn.bookingmeeting.service.user.admin;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.room.MeetingRoomResponse;
import com.dtsvn.bookingmeeting.dto.user.UserResponse;
import com.dtsvn.bookingmeeting.dto.user.admin.UserRequest;
import com.dtsvn.bookingmeeting.dto.user.admin.UserUpdateRequest;
import com.dtsvn.bookingmeeting.mapper.user.UserMapper;
import com.dtsvn.bookingmeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest request) {
        try {
            User user = userMapper.toEntity(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setActive(true);
            userRepository.save(user);
            return userMapper.toUserResponse(user);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = getUserByIdOrThrow(id);
        log.info("Retrieved user by id: {}", id);
        return  userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.info("Updating user with id: {}, request: {}", id, request);
        User user = getUserByIdOrThrow(id);
        userMapper.updateEntity(user, request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User user = getUserByIdOrThrow(id);
        userRepository.delete(user);
    }

    @Override
    public UserResponse changeUserStatus(Long id, boolean active) {
        log.info("Changing user status for id: {}, active: {}", id, active);
        User user = getUserByIdOrThrow(id);
        user.setActive(active);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDirection) {
        log.debug("Request to get all meeting rooms with pagination: page={}, size={}, sortBy={}, sortDirection={}",
            page, size, sortBy, sortDirection);

        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "id";
        if (sortDirection == null || sortDirection.trim().isEmpty()) sortDirection = "ASC";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        var pageResult = userRepository.findAll(pageable);
        List<UserResponse> userResponses = pageResult.getContent()
            .stream()
            .map(userMapper::toUserResponse)
            .toList();

        return userResponses;
    }

    @Override
    public UserResponse changeUserRole(Long id, Role role) {
        log.info("Changing user role for id: {}, role: {}", id, role);
        User user = getUserByIdOrThrow(id);
        user.setRole(role);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    private User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
}
