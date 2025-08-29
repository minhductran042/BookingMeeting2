package com.dtsvn.bookingmeeting.web.rest.user_device;

import com.dtsvn.bookingmeeting.domain.userDevice.UserDevice;
import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.notification.RegisterDeviceRequest;
import com.dtsvn.bookingmeeting.service.user_device.UserDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-devices")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Device Management", description = "APIs for managing user device tokens")
public class UserDeviceController {
    private final UserDeviceService userDeviceService;

    @PostMapping("/register")
    @Operation(summary = "Đăng ký device token", description = "Đăng ký device token để nhận thông báo")
    public ApiResponse<UserDevice> registerDevice(@Valid @RequestBody RegisterDeviceRequest request) {
        log.info("Registering device for user with token: {}", request.getDeviceToken());
        try {
            UserDevice userDevice = userDeviceService.registerDevice(request);
            return new ApiResponse<>(200, "Device registered successfully", userDevice);
        } catch (Exception e) {
            log.error("Error registering device: {}", e.getMessage());
            return new ApiResponse<>(400, "Error registering device: " + e.getMessage(), null);
        }
    }

    @PostMapping("/unregister")
    @Operation(summary = "Hủy đăng ký device token", description = "Hủy đăng ký device token")
    public ApiResponse<Void> unregisterDevice(@RequestParam String deviceToken) {
        log.info("Unregistering device with token: {}", deviceToken);
        try {
            userDeviceService.unregisterDevice(deviceToken);
            return new ApiResponse<>(200, "Device unregistered successfully", null);
        } catch (Exception e) {
            log.error("Error unregistering device: {}", e.getMessage());
            return new ApiResponse<>(400, "Error unregistering device: " + e.getMessage(), null);
        }
    }

    @GetMapping("/my-devices")
    @Operation(summary = "Lấy danh sách devices", description = "Lấy tất cả devices đã đăng ký của user")
    public ApiResponse<List<UserDevice>> getMyDevices() {
        log.info("Getting devices for current user");
        try {
            List<UserDevice> devices = userDeviceService.getUserDevices();
            return new ApiResponse<>(200, "Devices retrieved successfully", devices);
        } catch (Exception e) {
            log.error("Error getting devices: {}", e.getMessage());
            return new ApiResponse<>(400, "Error getting devices: " + e.getMessage(), null);
        }
    }
}
