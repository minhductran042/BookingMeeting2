package com.dtsvn.bookingmeeting.dto.auth;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Common DTO for user information used across authentication responses.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long id;

    private String username;

    private String email;

    private String fullName;

    private Role role;

    private boolean isActive;

    private Collection<? extends GrantedAuthority> authorities;
}
