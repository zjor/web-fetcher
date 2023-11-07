package com.github.zjor.webfetcher.controller;


import com.github.zjor.webfetcher.dto.ScraperRequest;
import com.github.zjor.webfetcher.dto.ScraperResponse;
import com.github.zjor.webfetcher.ext.spring.aop.Log;
import com.github.zjor.webfetcher.model.Request;
import com.github.zjor.webfetcher.service.ScraperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scraper")
public class ScraperController {

    private final ScraperService service;

    @Log
    @PostMapping("/submit")
    public ResponseEntity<ScraperResponse> submit(@RequestBody @Valid ScraperRequest request) {
        return ResponseEntity.ok(service.submit(request));
    }

    @Log
    @GetMapping("/id/{requestId}/status")
    public ResponseEntity<Request> status(@PathVariable UUID requestId,
                                          @RequestParam(required = false) Integer poll) {
        return ResponseEntity.ok(service.getStatus(requestId, poll));
    }

    @Log
    @GetMapping(value = "/id/{requestId}/content", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> content(@PathVariable UUID requestId) {
        return ResponseEntity.ok(service.getContent(requestId));
    }
}
