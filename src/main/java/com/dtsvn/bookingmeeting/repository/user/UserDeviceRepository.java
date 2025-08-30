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

    List<UserDevice> findByUser(User user);

    Optional<UserDevice> findByDeviceToken(String deviceToken);
    List<UserDevice> findByUserAndActiveTrue(User user);

    boolean existsByDeviceToken(String deviceToken);

    void deleteByDeviceToken(String deviceToken);
}
