package com.github.zjor.webfetcher.notification.listeners;

import com.github.zjor.webfetcher.dto.RequestDto;

public interface EventListener {
    void update(RequestDto requestDto);
}