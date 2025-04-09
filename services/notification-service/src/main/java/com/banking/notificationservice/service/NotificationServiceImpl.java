package com.banking.notificationservice.service;

import com.banking.notificationservice.dto.EmailDetails;
import com.banking.notificationservice.dto.NotificationRequestDto;
import com.banking.notificationservice.dto.NotificationResponseDto;
import com.banking.notificationservice.exception.NotificationNotFoundException;
import com.banking.notificationservice.model.Notification;
import com.banking.notificationservice.model.NotificationStatus;
import com.banking.notificationservice.model.NotificationType;
import com.banking.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public NotificationResponseDto createNotification(NotificationRequestDto notificationRequestDto) {
        Notification notification = new Notification();
        notification.setUserId(notificationRequestDto.getUserId());
        notification.setEmail(notificationRequestDto.getEmail());
        notification.setSubject(notificationRequestDto.getSubject());
        notification.setContent(notificationRequestDto.getContent());
        notification.setType(notificationRequestDto.getType());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);
        
        return mapToDto(savedNotification);
    }

    @Override
    public NotificationResponseDto getNotificationById(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        return mapToDto(notification);
    }

    @Override
    public List<NotificationResponseDto> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDto> getNotificationsByEmail(String email) {
        List<Notification> notifications = notificationRepository.findByEmail(email);
        return notifications.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDto> getNotificationsByType(NotificationType type) {
        List<Notification> notifications = notificationRepository.findByType(type);
        return notifications.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDto> getNotificationsByStatus(NotificationStatus status) {
        List<Notification> notifications = notificationRepository.findByStatus(status);
        return notifications.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDto> getNotificationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Notification> notifications = notificationRepository.findByCreatedAtBetween(startDate, endDate);
        return notifications.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Page<NotificationResponseDto> getNotificationsByUserId(Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByUserId(userId, pageable);
        return notifications.map(this::mapToDto);
    }

    @Override
    public Page<NotificationResponseDto> getNotificationsByEmail(String email, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByEmail(email, pageable);
        return notifications.map(this::mapToDto);
    }

    @Override
    public Page<NotificationResponseDto> getNotificationsByType(NotificationType type, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByType(type, pageable);
        return notifications.map(this::mapToDto);
    }

    @Override
    public Page<NotificationResponseDto> getNotificationsByStatus(NotificationStatus status, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByStatus(status, pageable);
        return notifications.map(this::mapToDto);
    }

    @Override
    public NotificationResponseDto updateNotificationStatus(String id, NotificationStatus status) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        
        notification.setStatus(status);
        if (status == NotificationStatus.SENT) {
            notification.setSentAt(LocalDateTime.now());
        }
        
        Notification updatedNotification = notificationRepository.save(notification);
        return mapToDto(updatedNotification);
    }

    @Override
    public void deleteNotification(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        
        notificationRepository.delete(notification);
    }

    @Override
    public void processNotification(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        
        if (notification.getStatus() != NotificationStatus.PENDING) {
            return;
        }
        
        EmailDetails emailDetails = new EmailDetails(
                notification.getEmail(),
                notification.getSubject(),
                notification.getContent(),
                null
        );
        
        boolean sent = emailService.sendSimpleMail(emailDetails);
        
        if (sent) {
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
        } else {
            notification.setStatus(NotificationStatus.FAILED);
        }
        
        notificationRepository.save(notification);
    }

    @Override
    @Scheduled(fixedRate = 60000) // Run every minute
    public void processAllPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository.findByStatus(NotificationStatus.PENDING);
        
        for (Notification notification : pendingNotifications) {
            EmailDetails emailDetails = new EmailDetails(
                    notification.getEmail(),
                    notification.getSubject(),
                    notification.getContent(),
                    null
            );
            
            boolean sent = emailService.sendSimpleMail(emailDetails);
            
            if (sent) {
                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(LocalDateTime.now());
            } else {
                notification.setStatus(NotificationStatus.FAILED);
            }
            
            notificationRepository.save(notification);
        }
    }
    
    private NotificationResponseDto mapToDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setEmail(notification.getEmail());
        dto.setSubject(notification.getSubject());
        dto.setContent(notification.getContent());
        dto.setType(notification.getType());
        dto.setStatus(notification.getStatus());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setSentAt(notification.getSentAt());
        return dto;
    }
}
