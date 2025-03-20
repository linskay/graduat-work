package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "imageUrl", target = "imageUrl")
    User toUserDTO(ru.skypro.homework.model.User user);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "imageUrl", target = "imageUrl")
    ru.skypro.homework.model.User toUser(User userDTO);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    ru.skypro.homework.model.User toUser(Login login);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    ru.skypro.homework.model.User toUser(Register register);


    /**
     * Обновляет существующий объект User на основе DTO.
     *
     * @param updateUser DTO с новыми данными
     * @param user       существующий объект User
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "ads", ignore = true)
    void updateUserFromDTO(UpdateUser updateUser, @MappingTarget ru.skypro.homework.model.User user);

    //    @Mapping(source = "newPassword", target = "password")
//    User toUserPassword(NewPassword newPassword);
}