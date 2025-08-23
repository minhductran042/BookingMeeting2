package com.dtsvn.bookingmeeting.mapper.user;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.auth.UserInfo;
import com.dtsvn.bookingmeeting.dto.user.admin.UserRequest;
import com.dtsvn.bookingmeeting.dto.user.admin.UserUpdateRequest;
import com.dtsvn.bookingmeeting.dto.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper for converting between User entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest request);

    void updateEntity(@MappingTarget User user, UserUpdateRequest request);

    UserInfo toUserInfo(User user);

    UserResponse toUserResponse(User user);
}
