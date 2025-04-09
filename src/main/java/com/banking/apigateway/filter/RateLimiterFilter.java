package com.banking.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimiterFilter extends AbstractGatewayFilterFactory<RateLimiterFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);
    private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    public RateLimiterFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            
            // Get or create counter for this IP
            RequestCounter counter = requestCounters.computeIfAbsent(clientIp, 
                    ip -> new RequestCounter(config.getMaxRequestsPerMinute()));
            
            if (!counter.incrementAndCheckLimit()) {
                logger.warn("Rate limit exceeded for IP: {}", clientIp);
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
            
            return chain.filter(exchange);
        };
    }

    public static class Config {
        private int maxRequestsPerMinute = 60; // Default value

        public int getMaxRequestsPerMinute() {
            return maxRequestsPerMinute;
        }

        public void setMaxRequestsPerMinute(int maxRequestsPerMinute) {
            this.maxRequestsPerMinute = maxRequestsPerMinute;
        }
    }

    private static class RequestCounter {
        private final AtomicInteger counter = new AtomicInteger(0);
        private final int limit;
        private long resetTime;

        public RequestCounter(int limit) {
            this.limit = limit;
            this.resetTime = System.currentTimeMillis() + 60000; // Reset after 1 minute
        }

        public boolean incrementAndCheckLimit() {
            long currentTime = System.currentTimeMillis();
            
            // Reset counter if time window has passed
            if (currentTime > resetTime) {
                counter.set(0);
                resetTime = currentTime + 60000;
            }
            
            return counter.incrementAndGet() <= limit;
        }
    }
}
