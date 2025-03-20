package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ErrorMessages;
import ru.skypro.homework.exception.ImageUploadException;
import ru.skypro.homework.exception.UnauthorizedAccessException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private static final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);
    private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/uploads/images/ads/";

    private final AdRepository adRepository;
    private final UserService userService;
    private final AdMapper adMapper;

    @Override
    public Ads getAllAds() {
        logger.info("Fetching all ads");
        List<ru.skypro.homework.model.Ad> ads = adRepository.findAll();
        Ads response = new Ads();
        response.setCount(ads.size());
        response.setResults(adMapper.adsToAdDTOs(ads));
        return response;
    }

    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image) {
        try {
            // Получаем текущего пользователя
            User currentUser = userService.getCurrentUser();
                 log.info("Adding new ad by user: {}", currentUser.getEmail());

            // Проверяем, что изображение не пустое
            if (image == null || image.isEmpty()) {
                log.error(ErrorMessages.IMAGE_FILE_EMPTY);
                throw new ImageUploadException(ErrorMessages.IMAGE_FILE_EMPTY);
            }

            // Проверяем формат изображения
            if (!isImageFile(image)) {
                log.error(ErrorMessages.IMAGE_FILE_INVALID_FORMAT);
                throw new ImageUploadException(ErrorMessages.IMAGE_FILE_INVALID_FORMAT);
            }

            // Создаем объявление
            ru.skypro.homework.model.Ad ad = adMapper.createOrUpdateAdDTOToAd(properties);
            ad.setAuthor(currentUser); // Устанавливаем автора

            // Сохраняем изображение
            String imagePath = saveImage(image);
            ad.setImage(imagePath);

            // Сохраняем объявление в базе данных
            ru.skypro.homework.model.Ad savedAd = adRepository.save(ad);
            log.info("Ad added successfully with id: {}", savedAd.getId());

            return adMapper.adToAdDTO(savedAd);
        } catch (Exception e) {
            log.error("Error while adding ad: {}", e.getMessage(), e);
            throw new RuntimeException("Произошла внутренняя ошибка сервера", e);
        }
    }

    @Override
    public ExtendedAd getAd(Integer id) {
        logger.info("Fetching ad with id: {}", id);

        if (id == null) {
            logger.error("Ad ID is null");
            throw new IllegalArgumentException("Ad ID cannot be null");
        }

        ru.skypro.homework.model.Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(ErrorMessages.AD_NOT_FOUND);
                    return new AdNotFoundException(ErrorMessages.AD_NOT_FOUND);
                });

        return adMapper.adToExtendedAdDTO(ad);
    }

    @Override
    public void removeAd(Integer id) {
        logger.info("Removing ad with id: {}", id);
        ru.skypro.homework.model.Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(ErrorMessages.AD_NOT_FOUND);
                    return new AdNotFoundException(ErrorMessages.AD_NOT_FOUND);
                });

        User currentUser = userService.getCurrentUser();

        if (!ad.getAuthor().equals(currentUser) && !isAdmin(currentUser)) {
            logger.error(ErrorMessages.UNAUTHORIZED_ACCESS);
            throw new UnauthorizedAccessException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        adRepository.delete(ad);
        logger.info("Ad removed successfully with id: {}", id);
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd updatedAd) {
        logger.info("Updating ad with id: {}", id);
        ru.skypro.homework.model.Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(ErrorMessages.AD_NOT_FOUND);
                    return new AdNotFoundException(ErrorMessages.AD_NOT_FOUND);
                });

        User currentUser = userService.getCurrentUser();

        if (!ad.getAuthor().equals(currentUser) && !isAdmin(currentUser)) {
            logger.error(ErrorMessages.UNAUTHORIZED_ACCESS);
            throw new UnauthorizedAccessException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        ru.skypro.homework.model.Ad updatedEntity = adMapper.createOrUpdateAdDTOToAd(updatedAd);
        updatedEntity.setId(ad.getId());
        updatedEntity.setAuthor(ad.getAuthor());
        updatedEntity.setImage(ad.getImage());

        ru.skypro.homework.model.Ad savedAd = adRepository.save(updatedEntity);
        logger.info("Ad updated successfully with id: {}", id);

        return adMapper.adToAdDTO(savedAd);
    }

    @Override
    public List<Ad> getAdsMe() {
        User currentUser = userService.getCurrentUser();
        logger.info("Fetching ads for user: {}", currentUser.getEmail());

        return adMapper.adsToAdDTOs(adRepository.findByAuthor(currentUser));
    }

    @Override
    public byte[] updateImage(Integer id, MultipartFile image) {
        logger.info("Updating image for ad with id: {}", id);
        ru.skypro.homework.model.Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(ErrorMessages.AD_NOT_FOUND);
                    return new AdNotFoundException(ErrorMessages.AD_NOT_FOUND);
                });

        User currentUser = userService.getCurrentUser();

        if (!ad.getAuthor().equals(currentUser) && !isAdmin(currentUser)) {
            logger.error(ErrorMessages.UNAUTHORIZED_ACCESS);
            throw new UnauthorizedAccessException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        if (image == null || image.isEmpty()) {
            logger.error(ErrorMessages.IMAGE_FILE_EMPTY);
            throw new ImageUploadException(ErrorMessages.IMAGE_FILE_EMPTY);
        }

        try {
            String imagePath = saveImage(image);
            ad.setImage(imagePath);
            adRepository.save(ad);
            logger.info("Image updated successfully for ad with id: {}", id);
            return image.getBytes();
        } catch (IOException e) {
            logger.error(ErrorMessages.IMAGE_UPLOAD_FAILED, e);
            throw new ImageUploadException(ErrorMessages.IMAGE_UPLOAD_FAILED, e);
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(IMAGE_UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(image.getInputStream(), filePath);

        return "/uploads/images/ads/" + fileName;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * Проверяет, является ли пользователь админом.
     *
     * @param user пользователь
     * @return true, если пользователь — админ, иначе false
     */
    private boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }
}