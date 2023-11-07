package com.github.zjor.webfetcher.client;

import com.github.zjor.webfetcher.model.Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Client {

    private final RestTemplate restTemplate;

    private ResponseEntity<String> sendPostRequest(String url, Request request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var requestEntity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public void sendStatusUpdate(Request request) {
        try {
            Optional.ofNullable(request.getWebhookUrl())
                    .ifPresent(url -> {
                        sendPostRequest(url, request);
                        log.info("Status update request was sent to {}", url);
                    });
        } catch (Exception e) {
            log.error("Something went wrong when sending post request to {}. Error: {}", request.getWebhookUrl(), e.getMessage());
        }
    }
}
