package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.util.List;

public interface AdService {

    Ads getAllAds();

    Ad addAd(CreateOrUpdateAd properties, MultipartFile image);
    ExtendedAd getAd(Integer id);
    void removeAd(Integer id);
    Ad updateAd(Integer id, CreateOrUpdateAd updatedAd);
    List<Ad> getAdsMe();
    byte[] updateImage(Integer id, MultipartFile image);
}