package com.dtsvn.bookingmeeting.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


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


    public static NotificationRequest of(String title, String body) {
        NotificationRequest request = new NotificationRequest();
        request.setTitle(title);
        request.setBody(body);
        return request;
    }

}
