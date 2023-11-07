package com.github.zjor.webfetcher.service;


import com.github.zjor.webfetcher.client.Client;
import com.github.zjor.webfetcher.driver.Driver;
import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.model.Request;
import com.github.zjor.webfetcher.property.ScrapeProperty;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {
    private final RequestService requestService;
    private final ScrapeProperty property;
    private final BucketService bucketService;
    private final Client client;

    public void processRequest(Request request) {
        request.setLastStatus(RequestStatus.processing);
        updateRequest(request);

        var start = Instant.now();

        try (var driver = Driver.driverBuilder(property)) {
            var filename = String.format("%s.html", request.getId());
            var source = driver.fetchPageSource(request.getUrl());

            request.setDownloadUrl(bucketService.uploadFile(filename, source.getBytes()));
            request.setLastStatus(RequestStatus.ready);
            updateRequest(request);

        } catch (Exception e) {
            request.setError(e.getMessage());
            request.setLastStatus(RequestStatus.failed);
            updateRequest(request);

            log.error("Something went wrong when fetching the page source. Error: {}", e.getMessage());
        } finally {
            var end = Instant.now();
            log.info("Elapsed time: {} seconds", Duration.between(start, end).toSeconds());
        }
    }

    private void updateRequest(Request request) {
        var found = requestService.findRequest(request.getId());

        var currentStatus = request.getLastStatus();
        if (!found.getLastStatus().equals(currentStatus)) {
            sendWebhookUpdate(request);
            found.setLastStatus(currentStatus);
            log.info("Set status to {}", currentStatus);
        }

        Optional.ofNullable(request.getDownloadUrl())
                        .ifPresent(found::setDownloadUrl);

        requestService.save(request);
    }

    private void sendWebhookUpdate(Request request) {
        if (!StringUtils.isEmpty(request.getWebhookUrl())) {
            client.sendStatusUpdate(request);
        }
    }
}
