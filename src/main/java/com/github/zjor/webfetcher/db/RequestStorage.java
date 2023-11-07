package com.github.zjor.webfetcher.db;

import com.github.zjor.webfetcher.model.Request;
import com.github.zjor.webfetcher.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class RequestStorage {
    private final BlockingQueue<UUID> requestQueue;
    private final RequestService requestService;

    public RequestStorage(RequestService requestService) {
        this.requestQueue = new LinkedBlockingQueue<>();;
        this.requestService = requestService;
    }

    public boolean isEmptyQueue() {
        return requestQueue.isEmpty();
    }

    public void addRequest(@Valid Request request) {
        try {
            requestQueue.put(request.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Request take() {
        try {
            var reqId = requestQueue.take();
            return requestService.findRequest(reqId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
