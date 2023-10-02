package com.github.zjor.webfetcher.db;

import com.github.zjor.webfetcher.dto.Request;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RequestStorage {

    private final Map<UUID, Request> requestDb = new HashMap<>();
    private final Queue<UUID> requestQueue = new LinkedList<>();

    public boolean isEmptyQueue() {
       return requestQueue.isEmpty();
    }

    public void addRequest(@Valid Request newRequest) {
        requestDb.put(newRequest.getRequestId(), newRequest);
        requestQueue.add(newRequest.getRequestId());
    }

    public Request getRequest(UUID requestId) {
        return requestDb.get(requestId);
    }

    public Request removeRequest(UUID requestId) {
        return requestDb.remove(requestId);
    }

    public Request getNext() {
        if (!requestQueue.isEmpty()) {
            return requestDb.get(requestQueue.poll());
        }
        return null;
    }

}
