package com.github.zjor.webfetcher.util;

import com.backblaze.b2.client.contentHandlers.B2ContentSink;
import com.backblaze.b2.client.contentSources.B2Headers;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ContentHandler implements B2ContentSink {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @Override
    @SneakyThrows
    public void readContent(B2Headers responseHeaders, InputStream in) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
    }

    public byte[] getByteArray() {
        return byteArrayOutputStream.toByteArray();
    }

}