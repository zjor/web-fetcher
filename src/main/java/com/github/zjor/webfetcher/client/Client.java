package com.github.zjor.webfetcher.client;

import com.github.zjor.webfetcher.dto.Request;
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
public record Client(RestTemplate restTemplate) {


    public ResponseEntity<String> sendPostRequest(String url, Request requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public void sendStatusUpdate(Request request) {
        Optional.ofNullable(request.getWebHookUrl())
                .ifPresent(url -> sendPostRequest(url, request));
    }
}
