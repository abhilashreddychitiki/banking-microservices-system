package com.banking.notificationservice.controller;

import com.banking.notificationservice.dto.NotificationRequestDto;
import com.banking.notificationservice.dto.NotificationResponseDto;
import com.banking.notificationservice.model.NotificationStatus;
import com.banking.notificationservice.model.NotificationType;
import com.banking.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDto> createNotification(@Valid @RequestBody NotificationRequestDto notificationRequestDto) {
        NotificationResponseDto createdNotification = notificationService.createNotification(notificationRequestDto);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> getNotificationById(@PathVariable String id) {
        NotificationResponseDto notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByEmail(@PathVariable String email) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByEmail(email);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByType(@PathVariable NotificationType type) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByStatus(@PathVariable NotificationStatus status) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByStatus(status);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/paged")
    public ResponseEntity<Page<NotificationResponseDto>> getNotificationsByUserIdPaged(
            @PathVariable Long userId,
            Pageable pageable) {
        Page<NotificationResponseDto> notifications = notificationService.getNotificationsByUserId(userId, pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/email/{email}/paged")
    public ResponseEntity<Page<NotificationResponseDto>> getNotificationsByEmailPaged(
            @PathVariable String email,
            Pageable pageable) {
        Page<NotificationResponseDto> notifications = notificationService.getNotificationsByEmail(email, pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}/paged")
    public ResponseEntity<Page<NotificationResponseDto>> getNotificationsByTypePaged(
            @PathVariable NotificationType type,
            Pageable pageable) {
        Page<NotificationResponseDto> notifications = notificationService.getNotificationsByType(type, pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/status/{status}/paged")
    public ResponseEntity<Page<NotificationResponseDto>> getNotificationsByStatusPaged(
            @PathVariable NotificationStatus status,
            Pageable pageable) {
        Page<NotificationResponseDto> notifications = notificationService.getNotificationsByStatus(status, pageable);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<NotificationResponseDto> updateNotificationStatus(
            @PathVariable String id,
            @RequestParam NotificationStatus status) {
        NotificationResponseDto updatedNotification = notificationService.updateNotificationStatus(id, status);
        return ResponseEntity.ok(updatedNotification);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<Void> processNotification(@PathVariable String id) {
        notificationService.processNotification(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process-all")
    public ResponseEntity<Void> processAllPendingNotifications() {
        notificationService.processAllPendingNotifications();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
