package com.github.zjor.webfetcher.storage;

public interface StorageLocation {

    void store(String filename, byte[] fileData);
}
