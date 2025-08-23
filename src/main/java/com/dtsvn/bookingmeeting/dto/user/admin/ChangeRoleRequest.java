package com.dtsvn.bookingmeeting.dto.user.admin;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ChangeRoleRequest {
    Role role;
}
