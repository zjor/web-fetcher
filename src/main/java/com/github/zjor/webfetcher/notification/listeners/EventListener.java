package com.github.zjor.webfetcher.notification.listeners;

import com.github.zjor.webfetcher.dto.Request;

public interface EventListener {
    void update(Request request);
}