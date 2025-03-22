package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.UserEntity;

import java.io.IOException;
import java.util.List;

public interface AdService {

    Ads getAllAds();

 //  Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, UserEntity userEntity) throws IOException;

// Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) throws IOException;

    Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, UserEntity userEntity) throws IOException;

    ExtendedAd getAd(Integer id);
    void removeAd(Integer id);
    Ad updateAd(Integer id, CreateOrUpdateAd updatedAd);
    List<Ad> getAdsMe();
    byte[] updateImage(Integer id, MultipartFile image);
}