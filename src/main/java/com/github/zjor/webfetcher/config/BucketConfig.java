package com.github.zjor.webfetcher.config;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.backblaze.b2.client.contentSources.B2Headers.USER_AGENT;

@Configuration
public class BucketConfig {

    @Value("${bucket.key-id}")
    private String keyId;

    @Value("${bucket.application-key}")
    private String applicationKey;

    @Bean
    public B2StorageClient b2StorageClient() {
        return B2StorageClientFactory
                .createDefaultFactory()
                .create(keyId, applicationKey, USER_AGENT);
    }
}