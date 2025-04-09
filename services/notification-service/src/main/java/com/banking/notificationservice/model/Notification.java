package com.banking.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
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
