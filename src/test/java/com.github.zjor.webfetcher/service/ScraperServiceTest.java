package com.github.zjor.webfetcher.service;


import com.github.zjor.webfetcher.db.RequestStorage;
import com.github.zjor.webfetcher.dto.RequestDto;
import com.github.zjor.webfetcher.dto.ScraperRequest;
import com.github.zjor.webfetcher.service.impl.ScraperServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScraperServiceTest {
    @Mock
    private BucketService bucketService;
    @Mock
    private QueueService queueService;
    @Mock
    private RequestService requestService;
    @Spy
    private RequestStorage requestStorage;
    private ScraperService scrapeService;
    private static final String WEBHOOK_URL = "https://www.my-service.com/callback";
    private static final String URL = "https://www.amazon.com";
    private static final UUID ID = UUID.randomUUID();

    @BeforeEach
    void setup() {
        scrapeService = new ScraperServiceImpl(bucketService,
                requestStorage,
                queueService,
                requestService);
    }

    @Test
    void submitTest() {
        var apiRequest = mockApiRequest();

        var actualRequest = scrapeService.submit(apiRequest);
        var requestId = actualRequest.getRequestId();

        verify(requestStorage, times(1)).addRequest(any(RequestDto.class));

        assertNotNull(actualRequest);
        assertNotNull(requestStorage.getRequest(requestId));
        assertNotNull(requestStorage.take());

        assertEquals(WEBHOOK_URL, requestStorage.getRequest(requestId).getWebHookUrl());
        assertEquals(URL, requestStorage.getRequest(requestId).getUrlToDownload());
    }

    private RequestDto mockRequest() {
        return RequestDto.builder()
                .requestId(ID)
                .urlToDownload(URL)
                .webHookUrl(WEBHOOK_URL)
                .build();
    }

    private ScraperRequest mockApiRequest() {
        return ScraperRequest.builder()
                .url(URL)
                .webhookUrl(WEBHOOK_URL)
                .build();
    }

}
