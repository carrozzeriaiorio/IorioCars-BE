package com.ioriocars.ioriocars.controller;

import com.ioriocars.ioriocars.domain.Auto;
import com.ioriocars.ioriocars.service.AutoService;
import com.ioriocars.ioriocars.service.R2StorageService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Optional;

@RestController
@RequestMapping("/images")
@CrossOrigin(origins = {"http://localhost:4200", "https://ioriocars-fe.onrender.com"})
public class ImageController {

    private final R2StorageService r2StorageService;
    private final AutoService autoService;

    public ImageController(R2StorageService r2StorageService, AutoService autoService) {
        this.r2StorageService = r2StorageService;
        this.autoService = autoService;
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
