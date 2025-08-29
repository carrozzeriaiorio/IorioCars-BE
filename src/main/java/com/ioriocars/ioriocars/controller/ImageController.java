package com.ioriocars.ioriocars.controller;

import com.ioriocars.ioriocars.service.R2StorageService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.*;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final Path uploadDir = Paths.get("uploads/auto");

    private final R2StorageService r2StorageService;

    public ImageController(R2StorageService r2StorageService) {
        this.r2StorageService = r2StorageService;
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            byte[] data = r2StorageService.downloadFile(filename);

            String contentType = "application/octet-stream";

            // Restituisce il file con intestazione per download inline
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(data);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
