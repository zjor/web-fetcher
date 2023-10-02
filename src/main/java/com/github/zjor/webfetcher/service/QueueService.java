package com.github.zjor.webfetcher.service;

import com.github.zjor.webfetcher.dto.Request;

public interface QueueService {

    void processRequest(Request request);
}
