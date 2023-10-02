package com.github.zjor.webfetcher.service;

import com.github.zjor.webfetcher.dto.ContentResponse;
import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.dto.ScraperRequest;
import com.github.zjor.webfetcher.dto.ScraperResponse;

import java.util.UUID;

public interface ScraperService {
    ScraperResponse submit(ScraperRequest request);

    Request getStatus(UUID requestId, Integer poll);

    ContentResponse getContent(UUID requestId);

    void scrape();
}
