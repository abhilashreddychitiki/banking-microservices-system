package com.banking.apigateway.config;

import com.banking.apigateway.filter.AuthenticationFilter;
import com.banking.apigateway.filter.RateLimiterFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter authenticationFilter;
    
    @Autowired
    private RateLimiterFilter rateLimiterFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f
                                .filter(rateLimiterFilter.apply(new RateLimiterFilter.Config()))
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/api/users/(?<segment>.*)", "/${segment}"))
                        .uri("lb://user-service"))
                
                // Account Service Routes
                .route("account-service", r -> r.path("/api/accounts/**")
                        .filters(f -> f
                                .filter(rateLimiterFilter.apply(new RateLimiterFilter.Config()))
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/api/accounts/(?<segment>.*)", "/${segment}"))
                        .uri("lb://account-service"))
                
                // Transaction Service Routes
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .filters(f -> f
                                .filter(rateLimiterFilter.apply(new RateLimiterFilter.Config()))
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/api/transactions/(?<segment>.*)", "/${segment}"))
                        .uri("lb://transaction-service"))
                
                // Notification Service Routes
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .filters(f -> f
                                .filter(rateLimiterFilter.apply(new RateLimiterFilter.Config()))
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .rewritePath("/api/notifications/(?<segment>.*)", "/${segment}"))
                        .uri("lb://notification-service"))
                
                .build();
    }
}
