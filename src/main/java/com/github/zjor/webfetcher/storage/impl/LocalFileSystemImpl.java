package com.github.zjor.webfetcher.storage.impl;

import com.github.zjor.webfetcher.property.ScrapeProperty;
import com.github.zjor.webfetcher.storage.StorageStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@Primary
public record LocalFileSystemImpl(ScrapeProperty property) implements StorageStrategy {

    @Override
    @SneakyThrows
    public void store(UUID id, byte[] fileData) {
        var filename = String.format("%s/%s.html", property.sourceFilePath(), id);
        var path = System.getProperty("user.dir") + filename;
        try (var fos = new FileOutputStream(path)) {
            fos.write(fileData);
            log.info("File saved successfully on path {}", path);
        } catch (IOException e) {
            log.error("Something went wrong when writing the file");
        }
    }
}
