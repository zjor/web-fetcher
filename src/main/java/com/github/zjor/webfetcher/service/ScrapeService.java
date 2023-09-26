package com.github.zjor.webfetcher.service;

import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.dto.ScrapeRequest;
import com.github.zjor.webfetcher.dto.ScrapeResponse;

import java.util.UUID;

public interface ScrapeService {
    ScrapeResponse submit(ScrapeRequest request);

    Request getStatus(UUID requestId, Integer poll);

    void scrape();
}
