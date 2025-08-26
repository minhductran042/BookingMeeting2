package com.dtsvn.bookingmeeting.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class NotificationRequest {
    @NotBlank
    private String title; // Tiêu đề thông báo
    @NotBlank
    private String body; // Nội dung thông báo
    private String imageUrl; // URL của hình ảnh đính kèm (nếu có)
    private Map<String, String> data = new HashMap<>(); // Dữ liệu tùy chỉnh kèm theo thông báo
}
