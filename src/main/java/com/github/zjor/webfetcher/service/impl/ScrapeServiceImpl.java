package com.github.zjor.webfetcher.service.impl;


import com.github.zjor.webfetcher.driver.Driver;
import com.github.zjor.webfetcher.dto.ScrapeRequest;
import com.github.zjor.webfetcher.dto.ScrapeResponse;
import com.github.zjor.webfetcher.dto.Status;
import com.github.zjor.webfetcher.property.ScrapeProperty;
import com.github.zjor.webfetcher.service.ScrapeService;
import com.github.zjor.webfetcher.storage.StorageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public record ScrapeServiceImpl(ScrapeProperty property,
                                StorageStrategy storageStrategy) implements ScrapeService {

    @Override
    public ScrapeResponse submit(ScrapeRequest request) {
        var start = Instant.now();
        var id = UUID.randomUUID();
        try (var driver = Driver.DriverBuilder(property)) {
            var source = driver.fetchPageSource(request.getUrl());
            storageStrategy.store(id, source.getBytes());
        } catch (Exception e) {
            log.error("Something went wrong");
        } finally {
            var end = Instant.now();
            log.info("Elapsed time: {} seconds", Duration.between(start, end).toSeconds());
        }

        // TODO web hook
        // TODO hashids
        return ScrapeResponse.builder()
                .requestId(id)
                .build();
    }

    @Override
    public Status getStatus(UUID requestId, int poll) {
        return null;
    }
}
