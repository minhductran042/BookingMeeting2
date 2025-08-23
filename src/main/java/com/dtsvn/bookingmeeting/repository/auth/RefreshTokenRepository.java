package com.dtsvn.bookingmeeting.repository.auth;

import com.dtsvn.bookingmeeting.domain.auth.RefreshToken;
import com.dtsvn.bookingmeeting.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // Tìm token để validate
    Optional<RefreshToken> findByToken(String token);

    // Xóa token sau khi sử dụng (single use)
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.token = :token")
    void deleteByToken(@Param("token") String token);

    // Logout all devices - xóa tất cả token của user
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);

    // Cleanup expired tokens (scheduled job)
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
