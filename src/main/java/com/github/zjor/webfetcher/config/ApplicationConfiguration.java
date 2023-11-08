package com.github.zjor.webfetcher.config;

import com.github.zjor.webfetcher.ext.spring.ContextRefreshedEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ContextRefreshedEventHandler contextRefreshedEventHandler() {
        return new ContextRefreshedEventHandler();
    }

}
