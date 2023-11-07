package com.github.zjor.webfetcher.service;

import com.github.zjor.webfetcher.model.Request;
import com.github.zjor.webfetcher.repository.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RequestService {

    private final RequestRepository repository;

    public Request save(Request request) {
        return repository.save(request);
    }

    public Request findRequest(UUID id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
