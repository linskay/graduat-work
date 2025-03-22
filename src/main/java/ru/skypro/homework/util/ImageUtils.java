package ru.skypro.homework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@Slf4j
public class ImageUtils {
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
    public String saveImage(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(uploadDirPath);
            Files.createDirectories(uploadDir);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить изображение", e);
        }
    }

    /**
     * Читает изображение как массив байтов.
     *
     * @param imagePath относительный путь к файлу
     * @return массив байтов
     */
    public byte[] getImageAsBytes(String imagePath) {
        try {
            // Удаляем базовый URL, чтобы получить только путь к файлу
            String relativePath = imagePath.replace(baseUrl, "");
            Path filePath = Paths.get(uploadDirPath + relativePath);

            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать изображение", e);
        }
    }
}