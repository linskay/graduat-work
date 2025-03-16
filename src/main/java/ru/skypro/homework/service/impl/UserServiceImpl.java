package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long updatePassword(NewPassword newPassword) {
        User user = getCurrentUserFromContext();

        // Проверяем, что текущий пароль верный
        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Текущий пароль неверный");
        }

        // Шифруем новый пароль и сохраняем
        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);

        return user.getId();
    }

    @Override
    public UserDTO getCurrentUser() {
        User user = getCurrentUserFromContext();
        return userMapper.toUserDTO(user);
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        User user = getCurrentUserFromContext();

        userMapper.toUser(updateUser);
        userRepository.save(user);

        return updateUser;
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        User user = getCurrentUserFromContext();
        user.setImageUrl("/images/new-avatar.jpg");
        userRepository.save(user);
    }

    // Получение текущего пользователя (заглушка)
    private User getCurrentUserFromContext() {
        //toDo Логика получения текущего пользователя из контекста безопасности
        return userRepository.findById(1L).orElseThrow(); // Заглушка
    }
}