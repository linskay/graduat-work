package ru.skypro.homework.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

public interface UserService {


    void updatePassword(NewPassword newPassword);

    @PreAuthorize("#username == authentication.principal.username")
    User getCurrentUser();

    @PreAuthorize("#username == authentication.principal.username")
    User updateUser(UpdateUser updateUser);

    @PreAuthorize("#username == authentication.principal.username")
    void updateUserImage(MultipartFile image);


}