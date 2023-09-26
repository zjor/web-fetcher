package com.github.zjor.webfetcher.controller;


import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.dto.ScrapeRequest;
import com.github.zjor.webfetcher.dto.ScrapeResponse;
import com.github.zjor.webfetcher.service.ScrapeService;
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
public class ScrapeController {

    private final ScrapeService service;

    @PostMapping("/submit")
    public ResponseEntity<ScrapeResponse> submit(@RequestBody @Valid ScrapeRequest request) {
        log.info("Received submit request {}", request);
        return ResponseEntity.ok(service.submit(request));
    }

    @GetMapping("/id/{id}/status")
    public ResponseEntity<Request> status(@PathVariable UUID id,
                                          @RequestParam(required = false) Integer poll) {
        log.info("Received status request with requestId {} and poll {}", id, poll);
        return ResponseEntity.ok(service.getStatus(id, poll));
    }

}
