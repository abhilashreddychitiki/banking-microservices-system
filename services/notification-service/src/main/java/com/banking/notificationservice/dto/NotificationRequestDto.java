package com.banking.notificationservice.dto;

import com.banking.notificationservice.model.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    @NotNull
    private Long userId;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String subject;
    
    @NotBlank
    private String content;
    
    @NotNull
    private NotificationType type;
}
