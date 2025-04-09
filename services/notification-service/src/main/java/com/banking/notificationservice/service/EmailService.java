package com.banking.notificationservice.service;

import com.banking.notificationservice.dto.EmailDetails;

public interface EmailService {
    boolean sendSimpleMail(EmailDetails details);
    
    boolean sendMailWithAttachment(EmailDetails details);
}
