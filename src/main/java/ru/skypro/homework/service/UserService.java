package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

import java.io.IOException;

public interface UserService {

    void changePassword(String username, NewPassword newPassword);

    User getUser();

    UpdateUser updateUser(UpdateUser updateUser);

    String updateUserImage(MultipartFile image) throws IOException;

    String saveImage(MultipartFile image) throws IOException;

    UserEntity getAuthenticatedUser();
}