package com.github.zjor.webfetcher.service.impl;

import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.model.Request;
import com.github.zjor.webfetcher.repository.RequestRepository;
import com.github.zjor.webfetcher.service.RequestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;

    @Override
    public void createRequest(Request request) {
        repository.save(request);
    }

    @Override
    public void updateRequestStatus(UUID requestId, RequestStatus status) {
        var request = repository.findById(requestId)
                .orElseThrow(EntityNotFoundException::new);
        request.setLastStatus(status);
        repository.save(request);
    }
}
