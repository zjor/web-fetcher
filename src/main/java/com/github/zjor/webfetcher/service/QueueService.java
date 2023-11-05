package com.github.zjor.webfetcher.service;

import com.github.zjor.webfetcher.dto.RequestDto;

public interface QueueService {

    void processRequest(RequestDto requestDto);
}
