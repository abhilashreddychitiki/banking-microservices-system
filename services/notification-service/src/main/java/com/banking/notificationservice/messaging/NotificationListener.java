package com.banking.notificationservice.messaging;

import com.banking.notificationservice.dto.NotificationRequestDto;
import com.banking.notificationservice.model.NotificationType;
import com.banking.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.ACCOUNT_QUEUE)
    public void handleAccountNotification(Map<String, Object> message) {
        logger.info("Received account notification: {}", message);
        try {
            Long userId = Long.valueOf(message.get("userId").toString());
            String email = (String) message.get("email");
            String eventType = (String) message.get("eventType");
            
            NotificationType notificationType;
            String subject;
            String content;
            
            switch (eventType) {
                case "ACCOUNT_CREATED":
                    notificationType = NotificationType.ACCOUNT_CREATED;
                    subject = "Your account has been created";
                    content = "Dear Customer, your account has been successfully created. Account Number: " + message.get("accountNumber");
                    break;
                case "BALANCE_LOW":
                    notificationType = NotificationType.BALANCE_LOW;
                    subject = "Low Balance Alert";
                    content = "Dear Customer, your account balance is below the minimum threshold. Current Balance: " + message.get("balance");
                    break;
                default:
                    notificationType = NotificationType.ACCOUNT_CREATED;
                    subject = "Account Notification";
                    content = "Dear Customer, there has been an update to your account.";
            }
            
            NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                    userId,
                    email,
                    subject,
                    content,
                    notificationType
            );
            
            notificationService.createNotification(notificationRequestDto);
        } catch (Exception e) {
            logger.error("Error processing account notification", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.TRANSACTION_QUEUE)
    public void handleTransactionNotification(Map<String, Object> message) {
        logger.info("Received transaction notification: {}", message);
        try {
            Long userId = Long.valueOf(message.get("userId").toString());
            String email = (String) message.get("email");
            String eventType = (String) message.get("eventType");
            
            NotificationType notificationType;
            String subject;
            String content;
            
            switch (eventType) {
                case "TRANSACTION_COMPLETED":
                    notificationType = NotificationType.TRANSACTION_COMPLETED;
                    subject = "Transaction Completed";
                    content = "Dear Customer, your transaction has been completed successfully. Transaction Reference: " + message.get("reference");
                    break;
                case "TRANSACTION_FAILED":
                    notificationType = NotificationType.TRANSACTION_FAILED;
                    subject = "Transaction Failed";
                    content = "Dear Customer, your transaction has failed. Transaction Reference: " + message.get("reference");
                    break;
                default:
                    notificationType = NotificationType.TRANSACTION_COMPLETED;
                    subject = "Transaction Notification";
                    content = "Dear Customer, there has been an update to your transaction.";
            }
            
            NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                    userId,
                    email,
                    subject,
                    content,
                    notificationType
            );
            
            notificationService.createNotification(notificationRequestDto);
        } catch (Exception e) {
            logger.error("Error processing transaction notification", e);
        }
    }
}
