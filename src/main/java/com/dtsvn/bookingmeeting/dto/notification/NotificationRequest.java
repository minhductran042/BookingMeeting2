package com.dtsvn.bookingmeeting.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    @NotBlank
    private String title; // Tiêu đề thông báo
    @NotBlank
    private String body; // Nội dung thông báo
    private String imageUrl; // URL của hình ảnh đính kèm (nếu có)
    private Map<String, String> data = new HashMap<>(); // Dữ liệu tùy chỉnh kèm theo thông báo

    /**
     * Thêm data vào notification
     */
    public NotificationRequest addData(String key, String value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }

    /**
     * Thêm data với giá trị số
     */
    public NotificationRequest addData(String key, Number value) {
        return addData(key, value.toString());
    }

    /**
     * Thêm data với giá trị boolean
     */
    public NotificationRequest addData(String key, Boolean value) {
        return addData(key, value.toString());
    }

    /**
     * Tạo notification request đơn giản
     */
    public static NotificationRequest of(String title, String body) {
        NotificationRequest request = new NotificationRequest();
        request.setTitle(title);
        request.setBody(body);
        return request;
    }

    /**
     * Tạo notification request với data
     */
    public static NotificationRequest of(String title, String body, Map<String, String> data) {
        NotificationRequest request = new NotificationRequest();
        request.setTitle(title);
        request.setBody(body);
        request.setData(data);
        return request;
    }
}
