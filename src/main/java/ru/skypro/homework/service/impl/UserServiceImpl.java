package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.AuthenticationUtils;
import ru.skypro.homework.util.ImageUtils;

import static ru.skypro.homework.exception.ErrorMessages.INVALID_PASSWORD;
import static ru.skypro.homework.exception.ErrorMessages.USER_NOT_FOUND;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationUtils authenticationUtils;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageUtils imageUtils;

    @Override
    public void changePassword(NewPassword newPassword) {
        log.debug("Запрос на смену пароля");

        UserEntity userEntity = getCurrentUser();

        if (!userEntity.getPassword().matches(newPassword.getCurrentPassword())) {
            log.warn(INVALID_PASSWORD);
            throw new InvalidPasswordException(INVALID_PASSWORD);
        }

        userEntity.setPassword(newPassword.getNewPassword());
        userRepository.save(userEntity);

        log.info("Пароль успешно изменен для пользователя с email: {}", userEntity.getEmail());
    }

    @Override
    public User getUser() {
        UserEntity userEntity = getCurrentUser();
        log.debug("Информация о пользователе успешно получена: {}", userEntity.getEmail());

        return userMapper.toUserDTO(userEntity);
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        log.debug("Получен запрос на обновление пользователя: {}", updateUser);

        UserEntity userEntity = getCurrentUser();
        userMapper.updateUserFromDTO(updateUser, userEntity);

        userRepository.save(userEntity);

        UpdateUser updatedUserDTO = userMapper.toUpdateUser(userEntity);
        log.info("Информация о пользователе успешно обновлена: {}", userEntity.getEmail());

        return updatedUserDTO;
    }

    @Override
    public void updateUserAvatar(MultipartFile image) {
        log.debug("Получен запрос на обновление аватара пользователя");

        UserEntity currentUser = getCurrentUser();
        String avatarUrl = imageUtils.saveImage(image);

        currentUser.setImageUrl(avatarUrl);
        userRepository.save(currentUser);

        log.info("Аватар успешно обновлен для пользователя с email: {}", currentUser.getEmail());
    }

    public UserEntity getCurrentUser() {
        UserEntity currentUser = authenticationUtils.getAuthenticatedUser();
        if (currentUser == null) {
            log.error(USER_NOT_FOUND.formatted("unknown"));
            throw new UserNotFoundException(USER_NOT_FOUND.formatted("unknown"));
        }
        return currentUser;
    }
}