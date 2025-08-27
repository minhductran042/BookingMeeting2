package com.dtsvn.bookingmeeting.service.UserDevice;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.domain.userDevice.UserDevice;
import com.dtsvn.bookingmeeting.dto.notification.RegisterDeviceRequest;
import com.dtsvn.bookingmeeting.repository.user.UserDeviceRepository;
import com.dtsvn.bookingmeeting.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceServiceImpl implements UserDeviceService{

    private final UserDeviceRepository userDeviceRepository;
    private final SecurityUtils securityUtils;

    /**
     * Đăng ký device token cho user hiện tại
     */
    public UserDevice registerDevice(RegisterDeviceRequest request) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();

        // Kiểm tra token đã tồn tại chưa
        if (userDeviceRepository.existsByDeviceToken(request.getDeviceToken())) {
            // Cập nhật thông tin device
            UserDevice existingDevice = userDeviceRepository.findByDeviceToken(request.getDeviceToken())
                    .orElseThrow(() -> new RuntimeException("Device not found"));

            existingDevice.setDeviceInfo(request.getDeviceInfo());
            existingDevice.setActive(true);

            return userDeviceRepository.save(existingDevice);
        }

        // Tạo device mới
        UserDevice newDevice = UserDevice.builder()
                .user(currentUser)
                .deviceToken(request.getDeviceToken())
                .deviceType("web") // Chỉ xử lý web browser
                .deviceInfo(request.getDeviceInfo())
                .active(true)
                .build();

        return userDeviceRepository.save(newDevice);
    }

    /**
     * Hủy đăng ký device token
     */
    public void unregisterDevice(String deviceToken) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();

        userDeviceRepository.findByDeviceToken(deviceToken)
                .ifPresent(device -> {
                    if (device.getUser().equals(currentUser)) {
                        device.setActive(false);
                        userDeviceRepository.save(device);
                        log.info("Device deactivated for user: {}", currentUser.getEmail());
                    }
                });
    }

    /**
     * Lấy tất cả devices của user hiện tại
     */
    public List<UserDevice> getUserDevices() {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        return userDeviceRepository.findByUser(currentUser);
    }

    /**
     * Lấy tất cả active devices của user (cho NotificationService sử dụng)
     */
    public List<UserDevice> getUserActiveDevices(User user) {
        return userDeviceRepository.findByUserAndActiveTrue(user);
    }
}
