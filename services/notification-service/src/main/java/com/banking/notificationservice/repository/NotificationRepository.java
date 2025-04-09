package com.banking.notificationservice.repository;

import com.banking.notificationservice.model.Notification;
import com.banking.notificationservice.model.NotificationStatus;
import com.banking.notificationservice.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(Long userId);
    
    List<Notification> findByEmail(String email);
    
    List<Notification> findByType(NotificationType type);
    
    List<Notification> findByStatus(NotificationStatus status);
    
    List<Notification> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    
    Page<Notification> findByEmail(String email, Pageable pageable);
    
    Page<Notification> findByType(NotificationType type, Pageable pageable);
    
    Page<Notification> findByStatus(NotificationStatus status, Pageable pageable);
}
