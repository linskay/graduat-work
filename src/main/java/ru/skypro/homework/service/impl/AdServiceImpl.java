package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        UserEntity userEntity = getCurrentUser();
        AdEntity adEntity = adMapper.createOrUpdateAdDTOToAd(createOrUpdateAd);
        adEntity.setAuthor(userEntity);
        AdEntity savedAdEntity = adRepository.save(adEntity);
        return savedAdEntity.getId();
    }

    @Override
    public ExtendedAd getExtendedAd(Integer id) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new
                        AdNotFoundException(id));
        UserEntity currentUser = getCurrentUser();
        if (!adEntity.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("Нет прав на просмотр этого объявления");
        }
        return adMapper.adEntityToExtendedAd(adEntity);
    }

    @Override
    public void deleteAd(Integer id) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new
                        AdNotFoundException(id));
        adRepository.delete(adEntity);
        log.info("Объявление успешно удалено с ID: {}", id);
    }

    @Override
    public Integer updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new
                        AdNotFoundException(id));
        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setPrice(createOrUpdateAd.getPrice());
        adEntity.setDescription(createOrUpdateAd.getDescription());
        AdEntity updatedAdEntity = adRepository.save(adEntity);
        return updatedAdEntity.getId();
    }

    @Override
    public Ads getAdsForCurrentUser() {
        UserEntity currentUser = getCurrentUser();
        List<AdEntity> adEntities = adRepository.findByAuthor_Email(currentUser.getEmail());
        List<Ad> ads = adEntities.stream()
                .map(adMapper::adEntityToAd)
                .toList();
        return new Ads(ads.size(), ads);
    }

    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntities = adRepository.findAll();
        List<Ad> ads = adEntities.stream()
                .map(adMapper::adEntityToAd)
                .toList();
        return new Ads(ads.size(), ads);
    }

    @SneakyThrows
    @Override
    public String updateAdImage(Integer adId, MultipartFile image) throws IOException {
        // 1. Находим объявление
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        // 2. Если было старое изображение - удаляем его
        if (ad.getImage() != null && !ad.getImage().isEmpty()) {
            Path oldImagePath = Paths.get(imageUtils.getUploadDirPath(), ad.getImage());
            Files.deleteIfExists(oldImagePath);
        }

        // 3. Сохраняем новое изображение
        String newImageName = imageUtils.saveImage(image);

        // 4. Обновляем запись в БД
        ad.setImage(newImageName);
        adRepository.save(ad);

        return newImageName;
    }
}