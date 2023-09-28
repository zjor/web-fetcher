package com.github.zjor.webfetcher.storage;

import java.util.UUID;

public interface StorageStrategy {

    void store(UUID id, byte[] fileData);
}
