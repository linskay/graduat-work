package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    @Override
    public List<AdDTO> getAllAds() {
        return adMapper.adsToAdDTOs(adRepository.findAll());
    }

    @Override
    public AdDTO addAd(CreateOrUpdateAdDTO properties, MultipartFile image) {
        User currentUser = getCurrentUser();

        Ad ad = adMapper.createOrUpdateAdDTOToAd(properties);
        ad.setAuthor(currentUser);

        try {
            ad.setImage(Arrays.toString(image.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }

        Ad savedAd = adRepository.save(ad);

        return adMapper.adToAdDTO(savedAd);
    }

    @Override
    public ExtendedAdDTO getAd(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        return adMapper.adToExtendedAdDTO(ad);
    }

    @Override
    public void removeAd(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        User currentUser = getCurrentUser();
        if (!ad.getAuthor().equals(currentUser)) {
            throw new RuntimeException("У вас нет прав для удаления этого объявления");
        }

        adRepository.delete(ad);
    }

    @Override
    public AdDTO updateAd(Integer id, CreateOrUpdateAdDTO updatedAd) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        User currentUser = getCurrentUser();
        if (!ad.getAuthor().equals(currentUser)) {
            throw new RuntimeException("У вас нет прав для редактирования этого объявления");
        }

        Ad updatedEntity = adMapper.createOrUpdateAdDTOToAd(updatedAd);
        updatedEntity.setId(ad.getId());
        updatedEntity.setAuthor(ad.getAuthor());
        updatedEntity.setImage(ad.getImage());

        Ad savedAd = adRepository.save(updatedEntity);

        return adMapper.adToAdDTO(savedAd);
    }

    @Override
    public List<AdDTO> getAdsMe() {
        User currentUser = getCurrentUser();
        return adMapper.adsToAdDTOs(adRepository.findByAuthor(currentUser));
    }

    @Override
    public byte[] updateImage(Integer id, MultipartFile image) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        User currentUser = getCurrentUser();
        if (!ad.getAuthor().equals(currentUser)) {
            throw new RuntimeException("У вас нет прав для обновления изображения этого объявления");
        }

        try {
            byte[] imageBytes = image.getBytes();
            ad.setImage(Arrays.toString(imageBytes));
            adRepository.save(ad);
            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }
    }

    private User getCurrentUser() {
        // ToDo: Логика получения текущего авторизованного пользователя
        return userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Пользователь не авторизован"));
    }
}