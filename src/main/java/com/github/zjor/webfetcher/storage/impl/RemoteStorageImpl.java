package com.github.zjor.webfetcher.storage.impl;

import com.github.zjor.webfetcher.storage.StorageLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RemoteStorageImpl implements StorageLocation {

    @Override
    public void store(String filename, byte[] fileData) {
        log.debug("Saved to a remote target");
    }
}
