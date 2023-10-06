package com.github.zjor.webfetcher.db;

import com.github.zjor.webfetcher.dto.Request;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class RequestStorage {

    private final Map<UUID, Request> requestDb = new HashMap<>();
    private final BlockingQueue<UUID> requestQueue = new LinkedBlockingQueue<>();

    public boolean isEmptyQueue() {
        return requestQueue.isEmpty();
    }

    public void addRequest(@Valid Request newRequest) {
        try {
            requestDb.put(newRequest.getRequestId(), newRequest);
            requestQueue.put(newRequest.getRequestId());
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Request getRequest(UUID requestId) {
        return requestDb.get(requestId);
    }

    public Request removeRequest(UUID requestId) {
        return requestDb.remove(requestId);
    }

    /**
     * Blocks and waits for the next request to be available.
     *
     * @return
     */
    public Request take() {
        try {
            var reqId = requestQueue.take();
            return requestDb.get(reqId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
