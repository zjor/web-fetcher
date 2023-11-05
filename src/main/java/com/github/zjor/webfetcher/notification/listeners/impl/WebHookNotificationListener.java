package com.github.zjor.webfetcher.notification.listeners.impl;

import com.github.zjor.webfetcher.client.Client;
import com.github.zjor.webfetcher.dto.RequestDto;
import com.github.zjor.webfetcher.enumeration.EventType;
import com.github.zjor.webfetcher.notification.listeners.EventListener;
import com.github.zjor.webfetcher.notification.publisher.EventManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Component
public class WebHookNotificationListener implements EventListener {

    private final Client client;

    public WebHookNotificationListener(Client client, EventManager eventManager) {
        this.client = client;
        eventManager.subscribe(EventType.WEBHOOK, this);
    }

    @Override
    public void update(RequestDto requestDto) {
        try {
            client.sendStatusUpdate(requestDto);
        } catch (ResourceAccessException e) {
            log.error("WebHook notification failed");
        }
    }

}