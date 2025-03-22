package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "author", source = "author.id")
    Ad adEntityToAd(AdEntity adEntity);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    ExtendedAd adEntityToExtendedAd(AdEntity adEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    AdEntity createOrUpdateAdDTOToAd(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    AdEntity adToAdEntity(Ad ad);
}