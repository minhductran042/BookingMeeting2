package com.dtsvn.bookingmeeting.service.user_device;

import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.domain.userDevice.UserDevice;
import com.dtsvn.bookingmeeting.dto.notification.RegisterDeviceRequest;

import java.util.List;

public interface UserDeviceService {
    public UserDevice registerDevice(RegisterDeviceRequest request);
    public void unregisterDevice(String deviceToken);
    public List<UserDevice> getUserDevices();
    public List<UserDevice> getUserActiveDevices(User user);
}
