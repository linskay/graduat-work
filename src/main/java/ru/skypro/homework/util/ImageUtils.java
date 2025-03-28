package ru.skypro.homework.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ImageProcessingException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@Data
public class ImageUtils {
    public ResponseEntity<byte[]> getImageAsBytes;
    @Value("${image.upload.dir}")
    private String uploadDirPath;

    @Value("${image.base.url}")
    private String baseUrl;


    /**
     * Сохраняет изображение в проект.
     *
     * @param file файл изображения
     * @return относительный URL для доступа к изображению
     */
    public String saveImage(MultipartFile file) throws IOException {
        Path uploadDir = Paths.get(uploadDirPath);
        Files.createDirectories(uploadDir);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;

    }}
