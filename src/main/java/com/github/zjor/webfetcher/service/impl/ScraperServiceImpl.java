package com.github.zjor.webfetcher.service.impl;


import com.github.zjor.webfetcher.db.RequestStorage;
import com.github.zjor.webfetcher.dto.ContentResponse;
import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.dto.ScraperRequest;
import com.github.zjor.webfetcher.dto.ScraperResponse;
import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.service.BucketService;
import com.github.zjor.webfetcher.service.QueueService;
import com.github.zjor.webfetcher.service.ScraperService;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ScraperServiceImpl implements ScraperService {

    private final BucketService bucketService;
    private final RequestStorage requestStorage;
    private final QueueService queueService;

    @Override
    public ScraperResponse submit(ScraperRequest apiRequest) {
        var id = UUID.randomUUID(); // TODO hashids

        addToMemoryStorage(id, apiRequest);

        return ScraperResponse.builder()
                .requestId(id)
                .build();
    }

    @Override
    @PostConstruct
    public void scrape() {
        var thread = new Thread(() -> {
            while (true) {
                if (!requestStorage.isEmptyQueue()) {
                    Optional.ofNullable(requestStorage.getNext())
                            .ifPresent(queueService::processRequest);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void addToMemoryStorage(UUID id, ScraperRequest apiRequest) {
        requestStorage.addRequest(Request.builder()
                .requestId(id)
                .urlToDownload(apiRequest.getUrl())
                .webHookUrl(apiRequest.getWebhookUrl())
                .build());
    }

    @Override
    public Request getStatus(UUID requestId, Integer poll) {
        var request = findRequest(requestId);
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

    private Request findRequest(UUID requestId) {
        return Optional.ofNullable(requestStorage.getRequest(requestId))
                .orElseThrow(() -> new NotFoundException(String.valueOf(requestId)));
    }

    @Override
    @SneakyThrows
    public ContentResponse getContent(UUID requestId) {
        var request = findRequest(requestId);
        String content = StringUtil.EMPTY_STRING;
        if (request.getStatus().equals(RequestStatus.ready)) {
            var contentBytes = bucketService.downloadFile(request.getDownloadUrl());
            content = new String(contentBytes, StandardCharsets.UTF_8);
        }
        return ContentResponse.builder()
                .requestId(requestId)
                .content(content)
                .build();
    }
}
