package com.github.zjor.webfetcher.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.model.Request;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDto {

    @NotNull
    private UUID requestId;
    private RequestStatus status;
    private String downloadUrl;
    private Error error;

    @JsonIgnore
    private String urlToDownload;
    @JsonIgnore
    private String webHookUrl;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String code;
        private String message;
    }

    public Request toModel() {
        return Request.builder()
                .id(requestId)
                .url(urlToDownload)
                .lastStatus(status)
                .webhook_url(webHookUrl)
                .build();
    }
}
