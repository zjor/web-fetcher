package com.github.zjor.webfetcher.storage.impl;

import com.github.zjor.webfetcher.storage.StorageLocation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
@Primary
public class LocalFileSystemImpl implements StorageLocation {

    @Override
    @SneakyThrows
    public void store(String filename, byte[] fileData) {
        var path = System.getProperty("user.dir") + filename;
        try (var fos = new FileOutputStream(path)) {
            fos.write(fileData);
            log.info("File saved successfully on path {}", path);
        } catch (IOException e) {
            log.error("Something went wrong when writing the file");
            throw e;
        }
    }
}
