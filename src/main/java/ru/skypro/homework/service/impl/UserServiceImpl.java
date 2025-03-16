package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.UserNotAuthorizedException;
import ru.skypro.homework.exception.UserNotFoundException;
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
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Long updatePassword(NewPassword newPassword) {
        ru.skypro.homework.model.User user = getCurrentUserFromContext();

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
    public User getCurrentUser() {
        // Получаем текущий контекст безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, что пользователь аутентифицирован
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthorizedException("Пользователь не авторизован");
        }

        // Получаем имя пользователя из Principal
        String username = authentication.getName();

        // Ищем пользователя в базе данных
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден в базе данных"));
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        ru.skypro.homework.model.User user = getCurrentUserFromContext();

        userMapper.toUser(updateUser);
        userRepository.save(user);

        return updateUser;
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        ru.skypro.homework.model.User user = getCurrentUserFromContext();
        user.setImageUrl("/images/new-avatar.jpg");
        userRepository.save(user);
    }

    // Получение текущего пользователя (заглушка)
    private ru.skypro.homework.model.User getCurrentUserFromContext() {
        // Получаем текущий контекст безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, что пользователь аутентифицирован
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Пользователь не авторизован");
            throw new UserNotAuthorizedException("Пользователь не авторизован");
        }

        // Получаем имя пользователя из Principal
        String username = authentication.getName();

        // Ищем пользователя в базе данных
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Пользователь не найден в базе данных: {}", username);
                    return new UserNotFoundException("Пользователь не найден в базе данных");
                });
    }
}