package com.dtsvn.bookingmeeting.service.auth;

import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import com.dtsvn.bookingmeeting.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Role extractRole(String token) {
        return extractClaim(token, claims -> {
            Object roleObj = claims.get("role");
            if (roleObj != null) {
                return Role.valueOf(roleObj.toString());
            }
            return null;
        });
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> {
            Object userIdObj = claims.get("userId");
            if (userIdObj != null) {
                return Long.valueOf(userIdObj.toString());
            }
            return null;
        });
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        // Nếu userDetails là User entity, thêm thông tin bổ sung
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            extraClaims.put("userId", user.getId());
            extraClaims.put("email", user.getEmail());
            extraClaims.put("role", user.getRole());
            extraClaims.put("tokenType", "ACCESS");
            // Có thể thêm role, name, etc.
            // extraClaims.put("role", user.getRole());
        }
        String token = generateAccessToken(extraClaims, userDetails, jwtExpiration);
        log.debug("Generated access token for user: {}", userDetails.getUsername());
        return token;
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            extraClaims.put("userId", user.getId());
            extraClaims.put("email", user.getEmail());
            extraClaims.put("role", user.getRole());
            extraClaims.put("tokenType", "REFRESH");
        }
        String refreshToken = generateAccessToken(extraClaims, userDetails, refreshExpiration);
        log.debug("Generated refresh token for user: {}", userDetails.getUsername());
        return refreshToken;
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return buildToken(extraClaims, userDetails, expiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        String subject;
        if (userDetails instanceof User) {
            subject = ((User) userDetails).getEmail();
        } else {
            subject = userDetails.getUsername();
        }

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String subject = extractUsername(token); // This is actually email from JWT
        if (userDetails instanceof User) {
            // Compare JWT subject (email) with User email
            return (subject.equals(((User) userDetails).getEmail())) && !isTokenExpired(token);
        } else {
            // Fallback for other UserDetails implementations
            return (subject.equals(userDetails.getUsername())) && !isTokenExpired(token);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
