package com.ioriocars.ioriocars.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class R2StorageService {

    private static final Logger log = LoggerFactory.getLogger(R2StorageService.class);

    private final S3Client s3Client;
    private final String bucketName;

    public R2StorageService(S3Client s3Client, @Value("${cloudflare.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = file.getOriginalFilename();

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        return key;
    }

    public byte[] downloadFile(String key) {
        ResponseBytes<GetObjectResponse> response = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                software.amazon.awssdk.core.sync.ResponseTransformer.toBytes()
        );

        return response.asByteArray();
    }

    public void deleteFile(String keyOrUrl) {
        if (keyOrUrl == null || keyOrUrl.isEmpty()) {
            log.warn("deleteFile called with null/empty key, skipping delete.");
            return;
        }

        // Estrai la chiave dal URL se necessario
        String key = extractKeyFromUrl(keyOrUrl);

        try {
            log.info("Attempting to delete key '{}' from bucket '{}', endpoint='{}'",
                    key, bucketName, s3Client.serviceClientConfiguration().endpointOverride().orElse(null));

            DeleteObjectResponse response = s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());

            log.info("DeleteObject response code: {}", response.sdkHttpResponse().statusCode());

        } catch (Exception e) {
            log.error("Failed to delete key '{}' from bucket '{}': {}", key, bucketName, e.getMessage(), e);
        }
    }

    private String extractKeyFromUrl(String keyOrUrl) {
        try {
            if (keyOrUrl.startsWith("http")) {
                URI uri = new URI(keyOrUrl);
                // Rimuove lo slash iniziale se presente
                return uri.getPath().startsWith("/") ? uri.getPath().substring(1) : uri.getPath();
            }
        } catch (URISyntaxException e) {
            log.warn("Invalid URL '{}', using as key directly.", keyOrUrl);
        }
        return keyOrUrl;
    }
}
