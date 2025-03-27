package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.exception.ImageProcessingException;
import ru.skypro.homework.util.ImageUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImagesController {

    private final ImageUtils imageUtils;

    /**
     * Получение ссылки на изображение.
     *
     * @param imageName имя файла изображения
     * @return полная ссылка на изображение
     */
    @GetMapping("/{imageName}")
    public ResponseEntity<String> getImageUrl(@PathVariable String imageName) {
        String relativePath = "/images/" + imageName;

        Path filePath = Paths.get(imageUtils.getUploadDirPath(), imageName);
        if (!Files.exists(filePath)) {
            throw new ImageProcessingException("Image not found");
        }

        String fullImageUrl = imageUtils.getBaseUrl() + relativePath;
        return ResponseEntity.ok(fullImageUrl);
    }
}