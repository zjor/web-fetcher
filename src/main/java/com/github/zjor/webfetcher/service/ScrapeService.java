package com.github.zjor.webfetcher.service;

import com.github.zjor.webfetcher.dto.ScrapeRequest;
import com.github.zjor.webfetcher.dto.ScrapeResponse;
import com.github.zjor.webfetcher.dto.Status;

import java.util.UUID;

public interface ScrapeService {
    ScrapeResponse submit(ScrapeRequest request);

    Status getStatus(UUID requestId, int poll);
}
