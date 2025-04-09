package com.banking.notificationservice.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ACCOUNT_QUEUE = "account.notification.queue";
    public static final String TRANSACTION_QUEUE = "transaction.notification.queue";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String ACCOUNT_ROUTING_KEY = "account.notification";
    public static final String TRANSACTION_ROUTING_KEY = "transaction.notification";

    @Bean
    public Queue accountQueue() {
        return new Queue(ACCOUNT_QUEUE, true);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(TRANSACTION_QUEUE, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding accountBinding(Queue accountQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(accountQueue).to(notificationExchange).with(ACCOUNT_ROUTING_KEY);
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(transactionQueue).to(notificationExchange).with(TRANSACTION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
