package com.dtsvn.bookingmeeting.security;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for security-related operations.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityUtils {

    private final UserRepository userRepository;


    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        String username = authentication.getName(); // This is actually the username
        log.debug("Getting current authenticated user with username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found with username: " + username));
    }


    public String getCurrentAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        String username = authentication.getName(); // This is actually the username
        log.debug("Getting current authenticated user username: {}", username);
        
        // Get user first, then return email
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found with username: " + username));
        return user.getEmail();
    }

    /**
     * Check if the current user has a specific role.
     *
     * @param role the role to check (e.g., "ADMIN", "USER")
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }

    /**
     * Check if the current user is an admin.
     *
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
}
