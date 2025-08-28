package com.dtsvn.bookingmeeting.security;

import com.dtsvn.bookingmeeting.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 * Uses SecurityContextHolder directly to avoid circular dependencies.
 */
@Component
@Slf4j
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.debug("No authentication found, using system auditor");
                return Optional.of(Constants.SYSTEM);
            }
            
            String username = authentication.getName();
            if (username == null || username.isEmpty() || "anonymousUser".equals(username)) {
                log.debug("Invalid username '{}', using system auditor", username);
                return Optional.of(Constants.SYSTEM);
            }
            
            log.debug("Using authenticated user '{}' as auditor", username);
            return Optional.of(username);
        } catch (Exception e) {
            log.warn("Error getting current auditor, using system as default: {}", e.getMessage());
            // If any error occurs, return system as default
            return Optional.of(Constants.SYSTEM);
        }
    }
}
