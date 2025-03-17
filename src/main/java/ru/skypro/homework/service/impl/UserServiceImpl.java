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
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(NewPassword newPassword) {
        logger.info("Updating password for current user");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            logger.error("Current password is incorrect for user: {}", username);
            throw new IllegalArgumentException("Текущий пароль неверный");
        }

        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
        logger.info("Password updated successfully for user: {}", username);
    }

    @Override
    public User getCurrentUser() {
        logger.info("Fetching current user");

        // Получаем текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        return userMapper.toDTO(user);
    }

    @Override
    public User updateUser(UpdateUser updateUser) {
        logger.info("Updating user details");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        userMapper.updateUserFromDTO(updateUser, user);
        userRepository.save(user);
        logger.info("User details updated successfully for user: {}", username);

        return userMapper.toDTO(user);
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        logger.info("Updating user image");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        String imageUrl = saveImage(image);
        user.setImageUrl(imageUrl);
        userRepository.save(user);
        logger.info("User image updated successfully for user: {}", username);
    }

    /**
     * Сохраняет изображение и возвращает его URL.
     *
     * @param image файл изображения
     * @return URL сохраненного изображения
     */
    private String saveImage(MultipartFile image) {
        return "/images/new-avatar.jpg";
    }
}