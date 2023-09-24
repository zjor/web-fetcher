package com.github.zjor.webfetcher.storage.impl;

import com.github.zjor.webfetcher.storage.StorageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class RemoteStorageImpl implements StorageStrategy {

    @Override
    public void store(UUID id, byte[] fileData) {
        log.debug("Saved to a remote target");
    }
}
