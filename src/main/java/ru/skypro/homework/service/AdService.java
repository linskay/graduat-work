package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;

public interface AdService {

    Integer createAd(CreateOrUpdateAd createOrUpdateAd);

    ExtendedAd getExtendedAd(Integer id);

    void deleteAd(Integer id);

    Integer updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd);

    Ads getAdsForCurrentUser();

    Ads getAllAds();

    String updateAdImage(Integer id, MultipartFile image) throws IOException;
}