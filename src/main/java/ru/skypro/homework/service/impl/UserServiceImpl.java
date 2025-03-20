package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.*;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void changePassword(String email, NewPassword newPassword) {
        log.debug("Начало смены пароля для пользователя: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", email);
                    return new UserNotFoundException("User not found: " + email);
                });

        log.debug("Пользователь найден: {}", user);

        String currentPassword = newPassword.getCurrentPassword();
        String encodedPasswordFromDB = user.getPassword();

        log.debug("Введенный текущий пароль: {}", currentPassword);
        log.debug("Пароль из базы данных: {}", encodedPasswordFromDB);

        if (!passwordEncoder.matches(currentPassword, encodedPasswordFromDB)) {
            log.warn("Неверный текущий пароль для пользователя: {}", email);
            throw new InvalidPasswordException("Invalid current password for user: " + email);
        }

        log.debug("Текущий пароль верен.");

        String newPasswordEncoded = passwordEncoder.encode(newPassword.getNewPassword());
        log.debug("Новый закодированный пароль: {}", newPasswordEncoded);

        user.setPassword(newPasswordEncoded);
        userRepository.save(user);
        log.debug("Новый пароль сохранен в базе данных.");

        log.info("Пароль успешно изменен для пользователя: {}", email);
    }

    public User getCurrentUser() {
        log.info("Fetching current user");
        User user = getCurrentUserFromContext();
        log.info("Fetched user: {}", user);
        return user;
    }

    private User getCurrentUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error(ErrorMessages.USER_NOT_AUTHORIZED);
            throw new UserNotAuthorizedException(ErrorMessages.USER_NOT_AUTHORIZED);
        }

        String username = authentication.getName();
        log.info("Fetching user from database: {}", username);

        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error(ErrorMessages.USER_NOT_FOUND);
                    return new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });
    }

    @Override
    public UpdateUser updateUser(String email, UpdateUser updateUser) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.warn("Пользователь не найден: {}", email);
                        return new UserNotFoundException("User not found: " + email);
                    });
            userMapper.updateUserFromDTO(updateUser, user);

            UpdateUser updatedUser = new UpdateUser();
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setPhone(user.getPhone());

            log.info("Информация о пользователе успешно обновлена: {}", email);
            return updatedUser;
        } catch (Exception e) {
            log.error("Ошибка при обновлении информации о пользователе: {}", email, e);
            throw new DataIntegrityViolationException("Ошибка при обновлении данных пользователя", e);
        }
    }

    @Override
    public String saveImage(MultipartFile image) throws IOException {
        File uploadPath = new File(uploadDir).getAbsoluteFile();

        String fileName = UUID.randomUUID() + "." + getFileExtension(image.getOriginalFilename());

        File file = new File(uploadPath, fileName);
        image.transferTo(file);

        return fileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }

    @Override
    public void updateUserImage(MultipartFile image) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));

        String imagePath = saveImage(image);

        user.setImageUrl(imagePath);
        userRepository.save(user);
    }}