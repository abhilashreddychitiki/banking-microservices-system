package com.banking.notificationservice.dto;

import com.banking.notificationservice.model.NotificationStatus;
import com.banking.notificationservice.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private String id;
    private Long userId;
    private String email;
    private String subject;
    private String content;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
