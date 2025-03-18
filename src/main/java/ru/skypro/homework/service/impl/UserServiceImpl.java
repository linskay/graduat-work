package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.ImageUploadException;
import ru.skypro.homework.exception.UserNotAuthorizedException;
import ru.skypro.homework.exception.UserNotFoundException;
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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long updatePassword(NewPassword newPassword) {
        log.info("Updating password for current user");

        User currentUser = getCurrentUserFromContext();

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), currentUser.getPassword())) {
            log.error("Current password is incorrect for user: {}", currentUser.getUsername());
            throw new IllegalArgumentException("Текущий пароль неверный");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(currentUser);
        log.info("Password updated successfully for user: {}", currentUser.getUsername());

        return currentUser.getId();
    }

    @Override
    public User getCurrentUser() {
        log.info("Fetching current user");
        return getCurrentUserFromContext();
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        log.info("Updating user details");

        User currentUser = getCurrentUserFromContext();

        userMapper.updateUserFromDTO(updateUser, currentUser);
        userRepository.save(currentUser);
        log.info("User details updated successfully for user: {}", currentUser.getUsername());

        return updateUser;
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        logger.info("Updating user image");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        try {
            String imageUrl = saveImage(image);

            user.setImageUrl(imageUrl);
            userRepository.save(user);

            logger.info("User image updated successfully for user: {}", username);
        } catch (IOException e) {
            logger.error("Ошибка при сохранении изображения", e);
            throw new ImageUploadException("Ошибка при сохранении изображения", e);
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Файл изображения пуст");
        }

        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/images";
        File dir = new File(uploadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFileName = image.getOriginalFilename();
        String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : ".jpg";
        String uniqueFileName = UUID.randomUUID() + fileExtension;

        File file = new File(dir, uniqueFileName);

        image.transferTo(file);

        return "/uploads/images/" + uniqueFileName;
    }

    /**
     * Получает текущего аутентифицированного пользователя из контекста безопасности.
     *
     * @return текущий пользователь
     * @throws UserNotAuthorizedException если пользователь не авторизован
     * @throws UserNotFoundException      если пользователь не найден в базе данных
     */
    private User getCurrentUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("User not authorized");
            throw new UserNotAuthorizedException("Пользователь не авторизован");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found in database: {}", username);
                    return new UserNotFoundException("Пользователь не найден в базе данных");
                });
    }
}