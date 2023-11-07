package com.github.zjor.webfetcher.service;


import com.github.zjor.webfetcher.db.RequestStorage;
import com.github.zjor.webfetcher.dto.ScraperRequest;
import com.github.zjor.webfetcher.dto.ScraperResponse;
import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.ext.spring.aop.Log;
import com.github.zjor.webfetcher.model.Request;
import io.netty.util.internal.StringUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScraperService implements Runnable {

    private final BucketService bucketService;
    private final RequestStorage requestStorage;
    private final QueueService queueService;

    private final RequestService requestService;

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    public ScraperResponse submit(ScraperRequest apiRequest) {
        var request = Request.builder()
                .lastStatus(RequestStatus.pending)
                .url(apiRequest.getUrl())
                .webhookUrl(apiRequest.getWebhookUrl())
                .build();

        var saved = requestService.save(request);
        requestStorage.addRequest(saved);

        return ScraperResponse.builder()
                .requestId(saved.getId())
                .build();
    }

    @Log
    @PostConstruct
    public void scrape() {
        executor.submit(this);
    }

    public Request getStatus(UUID requestId, Integer poll) {
        var request = findRequest(requestId);
        Optional.ofNullable(poll)
                .filter(p -> request.getLastStatus().equals(RequestStatus.processing))
                .ifPresent(seconds -> {
                    try {
                        log.info("Request id {} is in processing. Waiting {} seconds", request.getId(), seconds);
                        Thread.sleep(seconds * 1000);
                    } catch (InterruptedException e) {
                        log.error("Something went wrong when executing poll waiting");
                    }
                });
        return request;
    }

    @SneakyThrows
    public String getContent(UUID requestId) {
        var request = findRequest(requestId);
        if (!request.getLastStatus().equals(RequestStatus.ready)) {
            return StringUtil.EMPTY_STRING;
        }
        var contentBytes = bucketService.downloadFile(request.getUrl());
        return new String(contentBytes, StandardCharsets.UTF_8);
    }

    private Request findRequest(UUID requestId) {
        return Optional.ofNullable(requestService.findRequest(requestId))
                .orElseThrow(() -> new NotFoundException(String.valueOf(requestId)));
    }

    public void run() {
        while (true) {
            var req = requestStorage.take();
            queueService.processRequest(req);
        }
    }
}
