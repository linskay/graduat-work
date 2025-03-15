package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;

public interface UserService {
    Long updatePassword(NewPassword newPassword);
    UserDTO getCurrentUser();
    UpdateUser updateUser(UpdateUser updateUser);
    void updateUserImage(MultipartFile image);
}