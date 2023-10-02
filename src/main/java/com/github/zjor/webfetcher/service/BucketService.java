package com.github.zjor.webfetcher.service;

public interface BucketService {

    String uploadFile(String name, byte[] bytes);
    byte[] downloadFile(String name);
    void deleteFile(String name);
}
