package com.github.zjor.webfetcher.db;

import com.github.zjor.webfetcher.dto.RequestDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class RequestStorage {

    private final Map<UUID, RequestDto> requestDb = new HashMap<>();
    private final BlockingQueue<UUID> requestQueue = new LinkedBlockingQueue<>();

    public boolean isEmptyQueue() {
        return requestQueue.isEmpty();
    }

    public void addRequest(@Valid RequestDto newRequest) {
        try {
            requestDb.put(newRequest.getRequestId(), newRequest);
            requestQueue.put(newRequest.getRequestId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public RequestDto getRequest(UUID requestId) {
        return requestDb.get(requestId);
    }

    public RequestDto removeRequest(UUID requestId) {
        return requestDb.remove(requestId);
    }

    /**
     * Blocks and waits for the next request to be available.
     *
     * @return
     */
    public RequestDto take() {
        try {
            var reqId = requestQueue.take();
            return requestDb.get(reqId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
