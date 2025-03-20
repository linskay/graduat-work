package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

import java.io.IOException;

public interface UserService {

    void changePassword(String username, NewPassword newPassword);

    User getCurrentUser();

    void updateUserImage(MultipartFile image) throws IOException;

    UpdateUser updateUser(String email, UpdateUser updateUser);

    String saveImage(MultipartFile image) throws IOException;
}