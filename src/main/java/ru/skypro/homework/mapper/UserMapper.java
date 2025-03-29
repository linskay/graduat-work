package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "image", target = "image")
    User toUserDTO(UserEntity userEntity);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(target = "image", source = "image", ignore = true)
    UserEntity toUser(User user);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    UserEntity toUser(Login login);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    UserEntity toUser(Register register);

    UpdateUser toUpdateUser(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(UpdateUser updateUser, @MappingTarget UserEntity userEntity);
}