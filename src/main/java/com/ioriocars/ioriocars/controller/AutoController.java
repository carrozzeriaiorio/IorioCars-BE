package com.ioriocars.ioriocars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioriocars.ioriocars.domain.Auto;
import com.ioriocars.ioriocars.service.AutoService;
import com.ioriocars.ioriocars.service.R2StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auto")
@CrossOrigin(origins = {"http://localhost:4200", "https://ioriocars-fe.onrender.com"})
public class AutoController {

    @Autowired
    private AutoService autoService;

    @Autowired
    private R2StorageService r2StorageService;

    @GetMapping("/filter")
    public Page<Auto> getFiltered(
            @RequestParam(defaultValue = "") String marca,
            @RequestParam(defaultValue = "") String modello,
            @RequestParam(defaultValue = "1900") int annoMin,
            @RequestParam(defaultValue = "2100") int annoMax,
            @RequestParam(defaultValue = "0") double prezzoMin,
            @RequestParam(defaultValue = "1000000") double prezzoMax,
            @RequestParam(defaultValue = "0") int kmMin,
            @RequestParam(defaultValue = "1000000") int kmMax,
            @RequestParam(defaultValue = "") String carburante,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return autoService.findFiltered(marca, modello, annoMin, annoMax, prezzoMin, prezzoMax, kmMin, kmMax, carburante, page, size);
    }

    @GetMapping
    public List<Auto> getAll() {
        return autoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auto> getById(@PathVariable Long id) {
        return autoService.getById(id)
                .map(auto -> ResponseEntity.ok(auto))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public Auto create(
            @RequestPart("auto") String autoJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        Auto auto = new ObjectMapper().readValue(autoJson, Auto.class);

        if (file != null && !file.isEmpty()) {
            // upload su R2
            String key = r2StorageService.uploadFile(file);

            // salvo solo la chiave nel DB
            auto.setImmagine(key);
        }

        return autoService.create(auto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Auto> update(
            @PathVariable Long id,
            @RequestPart("auto") String autoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "removeImage", defaultValue = "false") boolean removeImage
    ) throws IOException {
        Auto auto = new ObjectMapper().readValue(autoJson, Auto.class);

        Auto existing = autoService.getById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Rimuove immagine da R2 se richiesto
        if (removeImage && existing.getImmagine() != null) {
            r2StorageService.deleteFile(existing.getImmagine());
            auto.setImmagine(null);
        }

        if (file != null && !file.isEmpty()) {
            // upload su R2
            String key = r2StorageService.uploadFile(file);

            // salvo solo la chiave nel DB
            auto.setImmagine(key);
        }

        Auto updated = autoService.update(id, auto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Auto> existing = autoService.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Auto auto = existing.get();

        // Rimuove l'immagine dal filesystem
        if (auto.getImmagine() != null && !auto.getImmagine().isEmpty()) {
            r2StorageService.deleteFile(auto.getImmagine());
        }

        // Elimina l’auto dal DB
        autoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
