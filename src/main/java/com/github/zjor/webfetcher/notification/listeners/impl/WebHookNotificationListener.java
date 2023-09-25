package com.github.zjor.webfetcher.notification.listeners.impl;

import com.github.zjor.webfetcher.client.Client;
import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.enumeration.EventType;
import com.github.zjor.webfetcher.notification.listeners.EventListener;
import com.github.zjor.webfetcher.notification.publisher.EventManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Component
public class WebHookNotificationListener implements EventListener {

    private final Client client;

    @Autowired
    public WebHookNotificationListener(Client client, EventManager eventManager) {
        this.client = client;
        eventManager.subscribe(EventType.WEBHOOK, this);
    }

    @Override
    public void update(Request request) {
        try {
            client.sendStatusUpdate(request);
        } catch (ResourceAccessException e) {
            log.error("WebHook notification failed");
        }
    }

}