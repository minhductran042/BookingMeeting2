package com.dtsvn.bookingmeeting.security;

import com.dtsvn.bookingmeeting.config.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private final SecurityUtils securityUtils;

    public SpringSecurityAuditorAware(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of(securityUtils.getCurrentAuthenticatedUserEmail());
        } catch (IllegalStateException e) {
            // If user is not authenticated, return system as default
            return Optional.of(Constants.SYSTEM);
        }
    }
}
