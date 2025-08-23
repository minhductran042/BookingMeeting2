package com.dtsvn.bookingmeeting.config;

import com.dtsvn.bookingmeeting.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        
        // Log request details for debugging
        log.debug("Processing request: {} {}", request.getMethod(), request.getRequestURI());
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No valid Authorization header found, continuing without authentication");
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);
            
            if (userEmail == null) {
                log.warn("Could not extract username from JWT token");
                filterChain.doFilter(request, response);
                return;
            }
            
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("Loading user details for email: {}", userEmail);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    log.debug("JWT token is valid, setting authentication for user: {}", userEmail);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication set successfully for user: {}", userEmail);
                } else {
                    log.warn("JWT token is invalid for user: {}", userEmail);
                }
            } else {
                log.debug("Authentication already exists in SecurityContext");
            }
        } catch (Exception e) {
            log.error("Error processing JWT token", e);
            // Don't block the request, let Spring Security handle it
        }
        
        filterChain.doFilter(request, response);
    }
}

