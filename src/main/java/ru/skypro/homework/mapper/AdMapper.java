package ru.skypro.homework.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdMapper {

    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    @Named("mapUserToId")
    default Integer mapUserToId(UserEntity userEntity) {
        return (userEntity != null) ? Math.toIntExact(userEntity.getId()) : null;
    }

    @Named("adToAdDTO")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author", target = "author", qualifiedByName = "mapUserToId")
    Ad adToAdDTO(AdEntity adEntity);

    @IterableMapping(qualifiedByName = "adToAdDTO")
    List<Ad> adsToAdDTOs(List<AdEntity> adEntities);

//    @IterableMapping
//    @Mapping(target = "results", source = "adEntities", qualifiedByName = "adToAdDTO")
//    @Mapping(target = "count", expression = "java(adEntities.size())")
//    Ads adsToAdsDTO(List<AdEntity> adEntities);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "author", ignore = true)
    AdEntity createOrUpdateAdDTOToAd(CreateOrUpdateAd dto);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author", target = "author", qualifiedByName = "mapUserToId")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "title", target = "title")
    ExtendedAd adToExtendedAdDTO(AdEntity adEntity);
}