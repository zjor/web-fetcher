package com.github.zjor.webfetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ScraperApp {

    public static void main(String[] args) {
        SpringApplication.run(ScraperApp.class, args);
    }
}
