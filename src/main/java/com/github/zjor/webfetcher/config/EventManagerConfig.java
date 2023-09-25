package com.github.zjor.webfetcher.config;

import com.github.zjor.webfetcher.enumeration.EventType;
import com.github.zjor.webfetcher.notification.publisher.EventManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventManagerConfig {

    @Bean
    public EventManager eventManager() {
        return new EventManager(EventType.WEBHOOK);
    }
}
