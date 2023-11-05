package com.github.zjor.webfetcher.notification.editor;

import com.github.zjor.webfetcher.dto.RequestDto;
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
//    private final RequestService requestService; doesnt work with thread executor

    public void changeStatus(RequestStatus status, RequestDto requestDto) {
       requestDto.setStatus(status);
//       requestService.updateRequestStatus(requestDto.getRequestId(), status);

       log.info("Request with id {} set to status {}", requestDto.getRequestId(), status);
       events.notify(EventType.WEBHOOK, requestDto);
    }
}