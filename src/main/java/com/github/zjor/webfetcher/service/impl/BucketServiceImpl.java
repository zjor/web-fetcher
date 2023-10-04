package com.github.zjor.webfetcher.service.impl;


import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.contentSources.B2ByteArrayContentSource;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2DownloadByNameRequest;
import com.backblaze.b2.client.structures.B2UploadFileRequest;
import com.github.zjor.webfetcher.service.BucketService;
import com.github.zjor.webfetcher.util.ContentHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    @Value("${bucket.id}")
    private String bucketId;

    @Value("${bucket.name}")
    private String bucketName;

    private final B2StorageClient b2StorageClient;

    @Override
    @SneakyThrows
    public String uploadFile(String objectName, byte[] bytes) {
        upload(objectName, bytes);
        return getDownloadUrl(objectName);
    }

    private String getDownloadUrl(String objectName) {
        try {
            return b2StorageClient.getDownloadByNameUrl(bucketName, objectName);
        } catch (B2Exception exception) {
            throw new RuntimeException("Error getting download url for file {} from bucket - {}" + objectName + exception.getMessage());
        }
    }

    void upload(String objectName, byte[] bytes) {
        var contentSource = B2ByteArrayContentSource
                .builder(bytes)
                .build();
        try {
            b2StorageClient.uploadSmallFile(B2UploadFileRequest.builder(bucketId, objectName, "text",
                            contentSource)
                    .build());
            log.info("File uploaded to b2 storage successfully");
        } catch (B2Exception exception) {
            throw new RuntimeException("Error uploading file {} to bucket - {}" + objectName + exception.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public byte[] downloadFile(String objectName) {
        try {
            var handler = new ContentHandler();
            var request = B2DownloadByNameRequest.builder(bucketName, objectName).build();
            b2StorageClient.downloadByName(request, handler);
            return handler.getByteArray();
        } catch (B2Exception exception) {
            throw new RuntimeException("Error downloading file {} from bucket - {}" + objectName + exception.getMessage());
        }
    }

    @Override
    public void deleteFile(String objectName) {
        // todo
    }
}
