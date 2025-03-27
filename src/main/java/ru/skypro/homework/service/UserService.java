package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

public interface UserService {

    void changePassword(NewPassword newPassword);

    User getUser();

    UpdateUser updateUser(UpdateUser updateUser);

 //   byte[] updateUserAvatar(Long id, MultipartFile image);

    UserEntity getCurrentUser();

    String updateUserImage(MultipartFile image);

    //byte[] updateUserImage(Long id, MultipartFile image);
}