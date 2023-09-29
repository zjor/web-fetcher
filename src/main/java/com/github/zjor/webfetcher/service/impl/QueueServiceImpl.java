package com.github.zjor.webfetcher.service.impl;


import com.github.zjor.webfetcher.driver.Driver;
import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.notification.editor.Editor;
import com.github.zjor.webfetcher.property.ScrapeProperty;
import com.github.zjor.webfetcher.service.BucketService;
import com.github.zjor.webfetcher.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private final Editor editor;
    private final ScrapeProperty property;
    private final BucketService bucketService;

    @Override
    public void processRequest(Request request) {
        editor.changeStatus(RequestStatus.processing, request);
        var start = Instant.now();

        try (var driver = Driver.driverBuilder(property)) {
            var filename = String.format("%s.html", request.getRequestId());
            var source = driver.fetchPageSource(request.getUrlToDownload());

            bucketService.uploadFile(filename, source.getBytes());
            request.setDownloadUrl(filename);
            editor.changeStatus(RequestStatus.ready, request);

        } catch (Exception e) {
            request.setError(Request.Error.builder()
                    .code("BAD_URL")
                    .message(e.getMessage())
                    .build());
            editor.changeStatus(RequestStatus.failed, request);
            log.error("Something went wrong when fetching the page source");

        } finally {
            var end = Instant.now();
            log.info("Elapsed time: {} seconds", Duration.between(start, end).toSeconds());
        }
    }
}