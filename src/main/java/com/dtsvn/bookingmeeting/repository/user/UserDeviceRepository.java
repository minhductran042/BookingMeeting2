package com.dtsvn.bookingmeeting.repository.user;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.domain.userDevice.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository để quản lý device tokens của users
 */
@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    /**
     * Tìm tất cả devices của user
     */
    List<UserDevice> findByUser(User user);

    /**
     * Tìm device theo token
     */
    Optional<UserDevice> findByDeviceToken(String deviceToken);

    /**
     * Tìm devices active của user
     */
    List<UserDevice> findByUserAndActiveTrue(User user);

    /**
     * Kiểm tra token đã tồn tại chưa
     */
    boolean existsByDeviceToken(String deviceToken);

    /**
     * Xóa device theo token
     */
    void deleteByDeviceToken(String deviceToken);
}
