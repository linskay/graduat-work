package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ErrorMessages;
import ru.skypro.homework.exception.ImageUploadException;
import ru.skypro.homework.exception.UnauthorizedAccessException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    private final AdRepository adRepository;
    private final UserService userService;
    private final AdMapper adMapper;
    private UserRepository userRepository;

    @Override
    public Ads getAllAds() {
        logger.info("Fetching all ads");

        List<AdEntity> adEntities = adRepository.findAll();
        Ads response = new Ads();

        response.setCount(adEntities.size());
        response.setResults(adMapper.adsToAdDTOs(adEntities));
        return response;
    }
    /**
     * Метод для сохранения изображения.
     *
     * @param image Загруженное изображение.
     * @return Имя сохраненного файла.
     * @throws IOException Если произошла ошибка при сохранении файла.
     */
    public String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = UUID.randomUUID().toString() + "." + getFileExtension(image.getOriginalFilename());

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        return fileName;
    }

    /**
     * Метод для получения расширения файла.
     *
     * @param fileName Оригинальное имя файла.
     * @return Расширение файла.
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }

    @Override
    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, UserEntity userEntity) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Изображение не предоставлено");
        }

        String fileName = saveImage(image);

        AdEntity adEntity = adMapper.createOrUpdateAdDTOToAd(createOrUpdateAd);
        adEntity.setAuthor(userEntity);
        adEntity.setImage("/uploads/images/" + fileName);

        adEntity = adRepository.save(adEntity);

        log.info("Объявление успешно создано: {}", adEntity.getTitle());
        return adMapper.adToAdDTO(adEntity);
    }


    @Override
    public ExtendedAd getAd(Integer id) {
        logger.info("Fetching ad with id: {}", id);

        if (id == null) {
            logger.error("Ad ID is null");
            throw new IllegalArgumentException("Ad ID cannot be null");
        }

        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(ErrorMessages.AD_NOT_FOUND);
                    return new AdNotFoundException(ErrorMessages.AD_NOT_FOUND);
                });

        return adMapper.adToExtendedAdDTO(adEntity);
    }

    @Override
    public void removeAd(Integer id) {
        logger.info("Removing ad with id: {}", id);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(ErrorMessages.AD_NOT_FOUND);
                    return new AdNotFoundException(ErrorMessages.AD_NOT_FOUND);
                });

        UserEntity currentUserEntity = userService.getAuthenticatedUser();

        if (!adEntity.getAuthor().equals(currentUserEntity) && !isAdmin(currentUserEntity)) {
            logger.error(ErrorMessages.UNAUTHORIZED_ACCESS);
            throw new UnauthorizedAccessException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        adRepository.delete(adEntity);
        logger.info("Ad removed successfully with id: {}", id);
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd updatedAd) {
        logger.info("Updating ad with id: {}", id);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(ErrorMessages.AD_NOT_FOUND);
                    return new AdNotFoundException(ErrorMessages.AD_NOT_FOUND);
                });

        UserEntity currentUserEntity = userService.getAuthenticatedUser();

        if (!adEntity.getAuthor().equals(currentUserEntity) && !isAdmin(currentUserEntity)) {
            logger.error(ErrorMessages.UNAUTHORIZED_ACCESS);
            throw new UnauthorizedAccessException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        AdEntity updatedEntity = adMapper.createOrUpdateAdDTOToAd(updatedAd);
        updatedEntity.setId(adEntity.getId());
        updatedEntity.setAuthor(adEntity.getAuthor());
        updatedEntity.setImage(adEntity.getImage());

        AdEntity savedAdEntity = adRepository.save(updatedEntity);
        logger.info("Ad updated successfully with id: {}", id);

        return adMapper.adToAdDTO(savedAdEntity);
    }

    @Override
    public List<Ad> getAdsMe() {
        UserEntity currentUserEntity = userService.getAuthenticatedUser();
        logger.info("Fetching ads for user: {}", currentUserEntity.getEmail());

        return adMapper.adsToAdDTOs(adRepository.findByAuthor(currentUserEntity));
    }

    @Override
    public byte[] updateImage(Integer id, MultipartFile image) {
        logger.info("Updating image for ad with id: {}", id);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(ErrorMessages.AD_NOT_FOUND);
                    return new AdNotFoundException(ErrorMessages.AD_NOT_FOUND);
                });

        UserEntity currentUserEntity = userService.getAuthenticatedUser();

        if (!adEntity.getAuthor().equals(currentUserEntity) && !isAdmin(currentUserEntity)) {
            logger.error(ErrorMessages.UNAUTHORIZED_ACCESS);
            throw new UnauthorizedAccessException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        if (image == null || image.isEmpty()) {
            logger.error(ErrorMessages.IMAGE_FILE_EMPTY);
            throw new ImageUploadException(ErrorMessages.IMAGE_FILE_EMPTY);
        }

        try {
            String imagePath = saveImage(image);
            adEntity.setImage(imagePath);
            adRepository.save(adEntity);
            logger.info("Image updated successfully for ad with id: {}", id);
            return image.getBytes();
        } catch (IOException e) {
            logger.error(ErrorMessages.IMAGE_UPLOAD_FAILED, e);
            throw new ImageUploadException(ErrorMessages.IMAGE_UPLOAD_FAILED, e);
        }
    }


    /**
     * Проверяет, является ли пользователь админом.
     *
     * @param userEntity пользователь
     * @return true, если пользователь — админ, иначе false
     */
    private boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRole() == Role.ADMIN;
    }
}