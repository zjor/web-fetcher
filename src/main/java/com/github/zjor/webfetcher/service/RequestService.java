package com.github.zjor.webfetcher.service;

import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.model.Request;

import java.util.UUID;

public interface RequestService {

    void createRequest(Request request);

    void updateRequestStatus(UUID requestId, RequestStatus status);
}
