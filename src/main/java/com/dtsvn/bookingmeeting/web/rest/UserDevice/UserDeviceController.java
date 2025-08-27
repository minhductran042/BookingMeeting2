package com.dtsvn.bookingmeeting.web.rest.UserDevice;

import com.dtsvn.bookingmeeting.domain.userDevice.UserDevice;
import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.service.UserDevice.UserDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/user-devices")
@Slf4j
@RequiredArgsConstructor
public class UserDeviceController {
    private final UserDeviceService userDeviceService;

    @GetMapping("/register")
    public ApiResponse<UserDevice> registerDevice(String deviceToken, String deviceInfo) {
        UserDevice userDevice = userDeviceService.registerDevice(
                new com.dtsvn.bookingmeeting.dto.notification.RegisterDeviceRequest(deviceToken, deviceInfo)
        );
        return new ApiResponse<>(200, "Device registered successfully", userDevice);
    }

    @GetMapping("/unregister")
    public ApiResponse<Void> unregisterDevice(String deviceToken) {
        userDeviceService.unregisterDevice(deviceToken);
        return new ApiResponse<>(200, "Device unregistered successfully", null);
    }

    /**
     * Lấy danh sách devices của user hiện tại
     */
    @GetMapping("/my-devices")
    @Operation(summary = "Lấy danh sách devices", description = "Lấy tất cả devices đã đăng ký của user")
    public ResponseEntity<List<UserDevice>> getMyDevices() {
        List<UserDevice> devices = userDeviceService.getUserDevices();
        return ResponseEntity.ok(devices);
    }

}
