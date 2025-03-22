package ru.skypro.homework.mapper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.UserEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "imageUrl", target = "imageUrl")
    User toUserDTO(UserEntity userEntity);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "imageUrl", target = "imageUrl")
    UserEntity toUser(User userDTO);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    UserEntity toUser(Login login);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    UserEntity toUser(Register register);

    UpdateUser toUpdateUser(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "adEntities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(UpdateUser updateUser, @MappingTarget UserEntity userEntity);
}