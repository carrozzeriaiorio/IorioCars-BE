package com.ioriocars.controller;

import com.ioriocars.domain.Auto;
import com.ioriocars.service.AutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auto")
public class AutoController {

    @Autowired
    private AutoService autoService;

    private final Path uploadDir = Paths.get("uploads");

    public AutoController() throws IOException {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    @GetMapping
    public List<Auto> getAll() {
        return autoService.getAll();
    }

    @GetMapping("/{id}")
    public Auto getById(@PathVariable Long id) {
        return autoService.getById(id).orElse(null);
    }

    @PostMapping
    public Auto create(@RequestPart("auto") Auto auto,
                       @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        if (file != null) {
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path path = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            auto.setImmagine(filename);
        }
        return autoService.create(auto);
    }

    @PutMapping("/{id}")
    public Auto update(@PathVariable Long id,
                       @RequestPart("auto") Auto auto,
                       @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        if (file != null) {
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path path = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            auto.setImmagine(filename);
        }
        return autoService.update(id, auto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        autoService.delete(id);
    }
}
