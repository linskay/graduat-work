package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.util.AuthenticationUtils;
import ru.skypro.homework.util.ImageUtils;

import java.util.List;

import static ru.skypro.homework.exception.ErrorMessages.AD_NOT_FOUND;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AuthenticationUtils authenticationUtils;
    private final ImageUtils imageUtils;
    private final AdRepository adRepository;
    private final AdMapper adMapper;

    private UserEntity getCurrentUser() {
        return authenticationUtils.getAuthenticatedUser();
    }

    @Override
    public Integer createAd(CreateOrUpdateAd createOrUpdateAd) {
        log.debug("Запрос на создание объявления");

        UserEntity userEntity = getCurrentUser();

        AdEntity adEntity = adMapper.createOrUpdateAdDTOToAd(createOrUpdateAd);
        adEntity.setAuthor(userEntity);

        adEntity.setImage("default-ad.jpg");

        AdEntity savedAdEntity = adRepository.save(adEntity);

        log.info("Объявление успешно создано с ID: {}", savedAdEntity.getId());
        return savedAdEntity.getId();
    }

    @Override
    public ExtendedAd getExtendedAd(Integer id) {
        log.debug("Запрос на получение расширенной информации об объявлении с ID: {}", id);

        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(AD_NOT_FOUND.formatted(id));
                    return new AdNotFoundException(AD_NOT_FOUND.formatted(id));
                });

        UserEntity currentUser = getCurrentUser();
        if (!adEntity.getAuthor().equals(currentUser)) {
            log.warn("Попытка доступа к чужому объявлению. Пользователь: {}, Объявление: {}", currentUser.getEmail(), id);
            throw new AccessDeniedException("Нет прав на просмотр этого объявления");
        }

        log.info("Расширенная информация об объявлении успешно получена для ID: {}", id);
        return adMapper.adEntityToExtendedAd(adEntity);
    }

    @Override
    public void deleteAd(Integer id) {
        log.debug("Запрос на удаление объявления с ID: {}", id);

        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(AD_NOT_FOUND.formatted(id));
                    return new AdNotFoundException(AD_NOT_FOUND.formatted(id));
                });

        adRepository.delete(adEntity);
        log.info("Объявление успешно удалено с ID: {}", id);
    }

    @Override
    public Integer updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        log.debug("Запрос на обновление объявления с ID: {}", id);

        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(AD_NOT_FOUND.formatted(id));
                    return new AdNotFoundException(AD_NOT_FOUND.formatted(id));
                });

        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setPrice(createOrUpdateAd.getPrice());
        adEntity.setDescription(createOrUpdateAd.getDescription());

        AdEntity updatedAdEntity = adRepository.save(adEntity);
        log.info("Объявление успешно обновлено с ID: {}", updatedAdEntity.getId());

        return updatedAdEntity.getId();
    }

    @Override
    public Ads getAdsForCurrentUser() {
        log.debug("Запрос на получение объявлений текущего пользователя");

        UserEntity currentUser = getCurrentUser();
        List<AdEntity> adEntities = adRepository.findByAuthor_Email(currentUser.getEmail());

        List<Ad> ads = adEntities.stream()
                .map(adMapper::adEntityToAd)
                .toList();

        log.info("Получено {} объявлений для пользователя с email: {}", ads.size(), currentUser.getEmail());
        return new Ads(ads.size(), ads);
    }

    @Override
    public Ads getAllAds() {
        log.debug("Запрос на получение всех объявлений");

        List<AdEntity> adEntities = adRepository.findAll();
        List<Ad> ads = adEntities.stream()
                .map(adMapper::adEntityToAd)
                .toList();

        log.info("Получено {} объявлений", ads.size());
        return new Ads(ads.size(), ads);
    }

    @Override
    public byte[] updateAdImage(Integer id, MultipartFile image) {
        log.debug("Запрос на обновление изображения объявления с ID: {}", id);

        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(AD_NOT_FOUND.formatted(id));
                    return new AdNotFoundException(AD_NOT_FOUND.formatted(id));
                });

        String imageUrl = imageUtils.saveImage(image);
        adEntity.setImage(imageUrl);

        adRepository.save(adEntity);
        log.info("Изображение успешно обновлено для объявления с ID: {}", id);

        return imageUtils.getImageAsBytes(imageUrl);
    }
}