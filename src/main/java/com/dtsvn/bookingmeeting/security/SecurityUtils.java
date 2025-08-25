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

    /**
     * Get the current authenticated user from SecurityContext.
     * Note: In this project, the username in SecurityContext is actually the user's email.
     *
     * @return the current authenticated user
     * @throws IllegalStateException if user is not authenticated or not found
     */
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        
        String userEmail = authentication.getName(); // This is actually the email
        log.debug("Getting current authenticated user with email: {}", userEmail);
        
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found with email: " + userEmail));
    }

    /**
     * Get the current authenticated user's email from SecurityContext.
     * Note: In this project, the username in SecurityContext is actually the user's email.
     *
     * @return the current authenticated user's email
     * @throws IllegalStateException if user is not authenticated
     */
    public String getCurrentAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        
        String userEmail = authentication.getName(); // This is actually the email
        log.debug("Getting current authenticated user email: {}", userEmail);
        return userEmail;
    }

    /**
     * Get the current authenticated username from SecurityContext.
     * Note: This method is kept for backward compatibility, but returns the email.
     * Use getCurrentAuthenticatedUserEmail() for clarity.
     *
     * @return the current authenticated username (which is actually the email)
     * @throws IllegalStateException if user is not authenticated
     * @deprecated Use getCurrentAuthenticatedUserEmail() instead for clarity
     */
    @Deprecated
    public String getCurrentAuthenticatedUsername() {
        return getCurrentAuthenticatedUserEmail();
    }

    /**
     * Check if the current user has a specific role.
     *
     * @param role the role to check
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
