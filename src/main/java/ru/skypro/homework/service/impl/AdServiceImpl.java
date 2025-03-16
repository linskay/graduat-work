package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ImageUploadException;
import ru.skypro.homework.exception.UnauthorizedAccessException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private static final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    private final AdRepository adRepository;
    private final UserService userService;
    private final AdMapper adMapper;

    @Override
    public List<Ad> getAllAds() {
        logger.info("Fetching all ads");
        return adMapper.adsToAdDTOs(adRepository.findAll());
    }

    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image) {
        // Получаем текущего пользователя
        User currentUser = userService.getCurrentUser();
        logger.info("Adding new ad by user: {}", currentUser.getUsername());

        // Создаем объявление
        ru.skypro.homework.model.Ad ad = adMapper.createOrUpdateAdDTOToAd(properties);
        ad.setAuthor(currentUser);

        // Сохраняем изображение
        try {
            ad.setImage(Arrays.toString(image.getBytes()));
        } catch (IOException e) {
            logger.error("Failed to upload image", e);
            throw new ImageUploadException("Ошибка при загрузке изображения", e);
        }

        // Сохраняем объявление в базе данных
        ru.skypro.homework.model.Ad savedAd = adRepository.save(ad);
        logger.info("Ad added successfully with id: {}", savedAd.getId());

        return adMapper.adToAdDTO(savedAd);
    }

    @Override
    public ExtendedAd getAd(Integer id) {
        logger.info("Fetching ad with id: {}", id);
        ru.skypro.homework.model.Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ad not found with id: {}", id);
                    return new AdNotFoundException("Объявление не найдено");
                });
        return adMapper.adToExtendedAdDTO(ad);
    }

    @Override
    public void removeAd(Integer id) {
        logger.info("Removing ad with id: {}", id);
        ru.skypro.homework.model.Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ad not found with id: {}", id);
                    return new AdNotFoundException("Объявление не найдено");
                });

        // Получаем текущего пользователя
        User currentUser = userService.getCurrentUser();

        // Проверяем, является ли пользователь автором объявления или админом
        if (!ad.getAuthor().equals(currentUser) && !isAdmin(currentUser)) {
            logger.error("Unauthorized access to delete ad by user: {}", currentUser.getUsername());
            throw new UnauthorizedAccessException("У вас нет прав для удаления этого объявления");
        }

        // Удаляем объявление
        adRepository.delete(ad);
        logger.info("Ad removed successfully with id: {}", id);
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd updatedAd) {
        logger.info("Updating ad with id: {}", id);
        ru.skypro.homework.model.Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ad not found with id: {}", id);
                    return new AdNotFoundException("Объявление не найдено");
                });

        // Получаем текущего пользователя
        User currentUser = userService.getCurrentUser();

        // Проверяем, является ли пользователь автором объявления или админом
        if (!ad.getAuthor().equals(currentUser) && !isAdmin(currentUser)) {
            logger.error("Unauthorized access to update ad by user: {}", currentUser.getUsername());
            throw new UnauthorizedAccessException("У вас нет прав для редактирования этого объявления");
        }

        // Обновляем объявление
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
        // Получаем текущего пользователя
        User currentUser = userService.getCurrentUser();
        logger.info("Fetching ads for user: {}", currentUser.getUsername());

        // Возвращаем объявления текущего пользователя
        return adMapper.adsToAdDTOs(adRepository.findByAuthor(currentUser));
    }

    @Override
    public byte[] updateImage(Integer id, MultipartFile image) {
        logger.info("Updating image for ad with id: {}", id);
        ru.skypro.homework.model.Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ad not found with id: {}", id);
                    return new AdNotFoundException("Объявление не найдено");
                });

        // Получаем текущего пользователя
        User currentUser = userService.getCurrentUser();

        // Проверяем, является ли пользователь автором объявления или админом
        if (!ad.getAuthor().equals(currentUser) && !isAdmin(currentUser)) {
            logger.error("Unauthorized access to update image by user: {}", currentUser.getUsername());
            throw new UnauthorizedAccessException("У вас нет прав для обновления изображения этого объявления");
        }

        // Обновляем изображение
        try {
            byte[] imageBytes = image.getBytes();
            ad.setImage(Arrays.toString(imageBytes));
            adRepository.save(ad);
            logger.info("Image updated successfully for ad with id: {}", id);
            return imageBytes;
        } catch (IOException e) {
            logger.error("Failed to upload image", e);
            throw new ImageUploadException("Ошибка при загрузке изображения", e);
        }
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