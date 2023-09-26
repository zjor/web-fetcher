package com.github.zjor.webfetcher.service.impl;


import com.github.zjor.webfetcher.db.RequestStorage;
import com.github.zjor.webfetcher.driver.Driver;
import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.dto.ScrapeRequest;
import com.github.zjor.webfetcher.dto.ScrapeResponse;
import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.notification.editor.Editor;
import com.github.zjor.webfetcher.property.ScrapeProperty;
import com.github.zjor.webfetcher.service.ScrapeService;
import com.github.zjor.webfetcher.storage.StorageLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapeServiceImpl implements ScrapeService {

    private final ScrapeProperty property;
    private final StorageLocation storageLocation;
    private final RequestStorage requestStorage;
    private final Editor editor;

    @Override
    public ScrapeResponse submit(ScrapeRequest apiRequest) {
        var id = UUID.randomUUID(); // TODO hashids

        placeToMemoryStorage(id, apiRequest);

        return ScrapeResponse.builder()
                .requestId(id)
                .build();
    }

    @Override
    @Scheduled(fixedDelayString = "${scraper.schedule.delay}000")
    public void scrape() {
        Optional.ofNullable(requestStorage.getNext())
                .ifPresent(requestToProcess -> {
                            editor.changeStatus(RequestStatus.processing, requestToProcess);
                            var start = Instant.now();

                            try (var driver = Driver.driverBuilder(property)) {
                                var filename = String.format("%s/%s.html", property.sourceFilePath(), requestToProcess.getRequestId());
                                var source = driver.fetchPageSource(requestToProcess.getUrlToDownload());

                                storageLocation.store(filename, source.getBytes());
                                requestToProcess.setDownloadUrl(filename);
                                editor.changeStatus(RequestStatus.ready, requestToProcess);

                            } catch (Exception e) {
                                requestToProcess.setError(Request.Error.builder()
                                        .code("BAD_URL")
                                        .message(e.getMessage())
                                        .build());
                                editor.changeStatus(RequestStatus.failed, requestToProcess);
                                log.error("Something went wrong when fetching the page source");

                            } finally {
                                var end = Instant.now();
                                log.info("Elapsed time: {} seconds", Duration.between(start, end).toSeconds());
                            }
                        }
                );
    }

    private void placeToMemoryStorage(UUID id, ScrapeRequest apiRequest) {
        var request = Request.builder()
                .requestId(id)
                .urlToDownload(apiRequest.getUrl())
                .webHookUrl(apiRequest.getWebhookUrl())
                .build();
        requestStorage.addRequest(request);
    }

    @Override
    public Request getStatus(UUID requestId, Integer poll) {
        var request = Optional.ofNullable(requestStorage.getRequest(requestId))
                .orElseThrow(() -> new NotFoundException(String.valueOf(requestId)));
        Optional.ofNullable(poll)
                .filter(p -> request.getStatus().equals(RequestStatus.processing))
                .ifPresent(seconds -> {
                    try {
                        log.info("Request id {} is in processing. Waiting {} seconds", request.getRequestId(), seconds);
                        Thread.sleep(seconds * 1000);
                    } catch (InterruptedException e) {
                        log.error("Something went wrong when executing poll waiting");
                    }
                });
        return request;
    }
}
