package com.github.zjor.webfetcher.controller;


import com.github.zjor.webfetcher.dto.ContentResponse;
import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.dto.ScraperRequest;
import com.github.zjor.webfetcher.dto.ScraperResponse;
import com.github.zjor.webfetcher.service.ScraperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scraper")
public class ScraperController {

    private final ScraperService service;

    @PostMapping("/submit")
    public ResponseEntity<ScraperResponse> submit(@RequestBody @Valid ScraperRequest request) {
        log.info("Received submit request {}", request);
        return ResponseEntity.ok(service.submit(request));
    }

    @GetMapping("/id/{requestId}/status")
    public ResponseEntity<Request> status(@PathVariable UUID requestId,
                                          @RequestParam(required = false) Integer poll) {
        log.info("Received status request with requestId {} and poll {}", requestId, poll);
        return ResponseEntity.ok(service.getStatus(requestId, poll));
    }

    @GetMapping("/id/{requestId}/content")
    public ResponseEntity<ContentResponse> content(@PathVariable UUID requestId) {
        log.info("Received status request with requestId {}", requestId);
        return ResponseEntity.ok(service.getContent(requestId));
    }
}
