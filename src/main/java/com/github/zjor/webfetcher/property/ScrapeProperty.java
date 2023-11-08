package com.github.zjor.webfetcher.property;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scraper")
public record ScrapeProperty(String sourceFilePath,
                             Driver driver) {

    public record Driver(String url, String headless) {
    }
}
