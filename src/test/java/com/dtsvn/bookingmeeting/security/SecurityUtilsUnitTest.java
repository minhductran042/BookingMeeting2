package com.dtsvn.bookingmeeting.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import com.dtsvn.bookingmeeting.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * Test class for the {@link SecurityUtils} utility class.
 */
@ExtendWith(MockitoExtension.class)
class SecurityUtilsUnitTest {

    @Mock
    private UserRepository userRepository;

    private SecurityUtils securityUtils;

    @BeforeEach
    void setUp() {
        securityUtils = new SecurityUtils(userRepository);
    }

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentAuthenticatedUsername() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin@example.com", "admin"));
        SecurityContextHolder.setContext(securityContext);
        
        String username = securityUtils.getCurrentAuthenticatedUsername();
        assertThat(username).isEqualTo("admin@example.com");
    }

    @Test
    void testGetCurrentAuthenticatedUserEmail() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin@example.com", "admin"));
        SecurityContextHolder.setContext(securityContext);
        
        String email = securityUtils.getCurrentAuthenticatedUserEmail();
        assertThat(email).isEqualTo("admin@example.com");
    }

    @Test
    void testGetCurrentAuthenticatedUser() {
        User testUser = User.builder()
            .id(1L)
            .username("admin")
            .email("admin@example.com")
            .role(Role.ADMIN)
            .build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin@example.com", "admin"));
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
        
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        assertThat(currentUser).isEqualTo(testUser);
    }

    @Test
    void testHasRole() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin@example.com", "admin", authorities));
        SecurityContextHolder.setContext(securityContext);

        assertThat(securityUtils.hasRole("ADMIN")).isTrue();
        assertThat(securityUtils.hasRole("USER")).isFalse();
    }

    @Test
    void testIsAdmin() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin@example.com", "admin", authorities));
        SecurityContextHolder.setContext(securityContext);

        assertThat(securityUtils.isAdmin()).isTrue();
    }

    @Test
    void testIsNotAdmin() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user@example.com", "user", authorities));
        SecurityContextHolder.setContext(securityContext);

        assertThat(securityUtils.isAdmin()).isFalse();
    }
}
