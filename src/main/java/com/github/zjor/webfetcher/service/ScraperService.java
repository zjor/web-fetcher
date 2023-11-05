package com.github.zjor.webfetcher.service;

import com.github.zjor.webfetcher.dto.RequestDto;
import com.github.zjor.webfetcher.dto.ScraperRequest;
import com.github.zjor.webfetcher.dto.ScraperResponse;

import java.util.UUID;

public interface ScraperService {
    ScraperResponse submit(ScraperRequest request);

    RequestDto getStatus(UUID requestId, Integer poll);

    String getContent(UUID requestId);

    void scrape();
}
