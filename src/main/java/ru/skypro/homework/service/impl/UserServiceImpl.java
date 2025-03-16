package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Long updatePassword(NewPassword newPassword) {
        User user = getCurrentUserFromContext();
        user.setPassword(newPassword.getNewPassword());
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public UserDTO getCurrentUser() {
        User user = getCurrentUserFromContext();
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getImageUrl()
        );
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        User user = getCurrentUserFromContext();

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        userRepository.save(user);

        return new UpdateUser(
                user.getFirstName(),
                user.getLastName(),
                user.getPhone()
        );
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        User user = getCurrentUserFromContext();
        user.setImageUrl("/images/new-avatar.jpg");
        userRepository.save(user);
    }

    // получение текущего пользователя (заглушка)
    private User getCurrentUserFromContext() {
        //toDo логика получения текущего пользователя из контекста безопасности
        return userRepository.findById(1L).orElseThrow(); // заглушка
    }
}