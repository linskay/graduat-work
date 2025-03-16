package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;

import java.util.List;

public interface AdService {
    List<AdDTO> getAllAds();
    AdDTO addAd(CreateOrUpdateAdDTO properties, MultipartFile image);
    ExtendedAdDTO getAd(Integer id);
    void removeAd(Integer id);
    AdDTO updateAd(Integer id, CreateOrUpdateAdDTO updatedAd);
    List<AdDTO> getAdsMe();
    byte[] updateImage(Integer id, MultipartFile image);
}