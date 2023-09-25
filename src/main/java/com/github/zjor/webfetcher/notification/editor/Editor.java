package com.github.zjor.webfetcher.notification.editor;

import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.enumeration.EventType;
import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.notification.publisher.EventManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Editor {
    public final EventManager events;

    public void changeStatus(RequestStatus status, Request request) {
       request.setStatus(status);
       log.info("Request with id {} set to status {}", request.getRequestId(), status);
       events.notify(EventType.WEBHOOK, request);
    }
}