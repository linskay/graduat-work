package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public AdServiceImpl(AdRepository adRepository, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AdDTO> getAllAds() {
        return adRepository.findAll().stream()
                .map(this::mapToAdDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdDTO addAd(CreateOrUpdateAdDTO properties, MultipartFile image) {
        // Получаем текущего пользователя (предполагается, что он авторизован)
        User currentUser = getCurrentUser();

        // Создаем новое объявление
        Ad ad = new Ad();
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setDescription(properties.getDescription());
        ad.setAuthor(currentUser);

        try {
            ad.setImage(Arrays.toString(image.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }

        // Сохраняем объявление в базе данных
        Ad savedAd = adRepository.save(ad);

        // Возвращаем DTO созданного объявления
        return mapToAdDTO(savedAd);
    }

    @Override
    public ExtendedAdDTO getAd(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        return mapToExtendedAdDTO(ad);
    }

    @Override
    public void removeAd(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        // Проверяем, что текущий пользователь является автором объявления
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

        // Проверяем, что текущий пользователь является автором объявления
        User currentUser = getCurrentUser();
        if (!ad.getAuthor().equals(currentUser)) {
            throw new RuntimeException("У вас нет прав для редактирования этого объявления");
        }

        // Обновляем данные объявления
        ad.setTitle(updatedAd.getTitle());
        ad.setPrice(updatedAd.getPrice());
        ad.setDescription(updatedAd.getDescription());

        // Сохраняем обновленное объявление
        Ad savedAd = adRepository.save(ad);

        // Возвращаем DTO обновленного объявления
        return mapToAdDTO(savedAd);
    }

    @Override
    public List<AdDTO> getAdsMe() {
        User currentUser = getCurrentUser();
        return adRepository.findByAuthor(currentUser).stream()
                .map(this::mapToAdDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] updateImage(Integer id, MultipartFile image) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        // Проверяем, что текущий пользователь является автором объявления
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

    private AdDTO mapToAdDTO(Ad ad) {
        AdDTO adDTO = new AdDTO();
        adDTO.setPk(ad.getId());
        adDTO.setAuthor(Math.toIntExact(ad.getAuthor().getId()));
        adDTO.setTitle(ad.getTitle());
        adDTO.setPrice(ad.getPrice());
        return adDTO;
    }

    private ExtendedAdDTO mapToExtendedAdDTO(Ad ad) {
        ExtendedAdDTO extendedAdDTO = new ExtendedAdDTO();
        extendedAdDTO.setPk(ad.getId());
        extendedAdDTO.setAuthorFirstName(ad.getAuthor().getFirstName());
        extendedAdDTO.setAuthorLastName(ad.getAuthor().getLastName());
        extendedAdDTO.setDescription(ad.getDescription());
        extendedAdDTO.setEmail(ad.getAuthor().getEmail());
        extendedAdDTO.setPhone(ad.getAuthor().getPhone());
        extendedAdDTO.setPrice(ad.getPrice());
        extendedAdDTO.setTitle(ad.getTitle());
        return extendedAdDTO;
    }

    private User getCurrentUser() {
        //toDo логика получения текущего авторизованного пользователя
        return userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Пользователь не авторизован"));
    }
}