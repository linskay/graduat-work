package ru.skypro.homework.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdMapper {

    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    @Named("mapUserToId")
    default Integer mapUserToId(User user) {
        return (user != null) ? Math.toIntExact(user.getId()) : null;
    }

    @Named("adToAdDTO")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author", target = "author", qualifiedByName = "mapUserToId")
    AdDTO adToAdDTO(Ad ad);

    @IterableMapping(qualifiedByName = "adToAdDTO")
    List<AdDTO> adsToAdDTOs(List<Ad> ads);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "author", ignore = true)
    Ad createOrUpdateAdDTOToAd(CreateOrUpdateAdDTO dto);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author", target = "author", qualifiedByName = "mapUserToId")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "title", target = "title")
    ExtendedAdDTO adToExtendedAdDTO(Ad ad);
}