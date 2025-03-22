package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.ErrorMessages;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Метод для получения текущего авторизованного пользователя.
     *
     * @return Экземпляр пользователя.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USER_NOT_FOUND + email));
    }

    @Override
    public void changePassword(String email, NewPassword newPassword) {
        log.debug("Начало смены пароля для пользователя: {}", email);

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", email);
                    return new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        log.debug("Пользователь найден: {}", userEntity);

        String currentPassword = newPassword.getCurrentPassword();
        String encodedPasswordFromDB = userEntity.getPassword();

        if (!passwordEncoder.matches(currentPassword, encodedPasswordFromDB)) {
            log.warn("Неверный текущий пароль для пользователя: {}", email);
            throw new InvalidPasswordException("Invalid current password for user: " + email);
        }

        String newPasswordEncoded = passwordEncoder.encode(newPassword.getNewPassword());

        userEntity.setPassword(newPasswordEncoded);
        userRepository.save(userEntity);

        log.info("Пароль успешно изменен для пользователя: {}", email);
    }

    @Override
    public User getUser() {
        try {
            UserEntity userEntity = getAuthenticatedUser();

            log.debug("Получен пользователь: {}", userEntity);

            return userMapper.toUserDTO(userEntity);
        } catch (Exception e) {
            log.error("Ошибка при получении информации о пользователе: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении данных пользователя", e);
        }
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USER_NOT_FOUND + email));

            userMapper.updateUserFromDTO(updateUser, userEntity);

            log.debug("Обновленный пользователь перед сохранением: {}", userEntity);
            userRepository.save(userEntity);

            UpdateUser updatedUserDTO = userMapper.toUpdateUser(userEntity);
            log.debug("Обновленный пользователь, возвращаемый клиенту: {}", updatedUserDTO);
            return updatedUserDTO;

        } catch (Exception e) {
            log.error("Ошибка при обновлении информации о пользователе: {}", e);
            throw new RuntimeException("Ошибка при обновлении данных пользователя", e);
        }
    }

    @Override
    public String updateUserImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Изображение не предоставлено");
        }

        UserEntity userEntity = getAuthenticatedUser();

        String fileName = saveImage(image);

        userEntity.setImageUrl("/uploads/images/" + fileName);
        userRepository.save(userEntity);

        return "Аватар успешно обновлен";
    }

    /**
     * Метод для сохранения изображения в файловой системе.
     *
     * @param image Загруженное изображение.
     * @return Имя сохраненного файла.
     * @throws IOException Если произошла ошибка при сохранении файла.
     */
    public String saveImage(MultipartFile image) throws IOException {
        File uploadPath = new File(uploadDir).getAbsoluteFile();

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String fileName = UUID.randomUUID() + "." + getFileExtension(image.getOriginalFilename());

        File file = new File(uploadPath, fileName);
        image.transferTo(file);

        return fileName;
    }

    /**
     * Метод для получения расширения файла.
     *
     * @param fileName Оригинальное имя файла.
     * @return Расширение файла.
     */
    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }
}