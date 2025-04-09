package com.banking.notificationservice.service;

import com.banking.notificationservice.dto.NotificationRequestDto;
import com.banking.notificationservice.dto.NotificationResponseDto;
import com.banking.notificationservice.model.NotificationStatus;
import com.banking.notificationservice.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    NotificationResponseDto createNotification(NotificationRequestDto notificationRequestDto);
    
    NotificationResponseDto getNotificationById(String id);
    
    List<NotificationResponseDto> getNotificationsByUserId(Long userId);
    
    List<NotificationResponseDto> getNotificationsByEmail(String email);
    
    List<NotificationResponseDto> getNotificationsByType(NotificationType type);
    
    List<NotificationResponseDto> getNotificationsByStatus(NotificationStatus status);
    
    List<NotificationResponseDto> getNotificationsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<NotificationResponseDto> getNotificationsByUserId(Long userId, Pageable pageable);
    
    Page<NotificationResponseDto> getNotificationsByEmail(String email, Pageable pageable);
    
    Page<NotificationResponseDto> getNotificationsByType(NotificationType type, Pageable pageable);
    
    Page<NotificationResponseDto> getNotificationsByStatus(NotificationStatus status, Pageable pageable);
    
    NotificationResponseDto updateNotificationStatus(String id, NotificationStatus status);
    
    void deleteNotification(String id);
    
    void processNotification(String id);
    
    void processAllPendingNotifications();
}
