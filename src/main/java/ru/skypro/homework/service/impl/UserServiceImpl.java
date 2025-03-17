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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long updatePassword(NewPassword newPassword) {
        logger.info("Updating password for current user");

        User currentUser = getCurrentUserFromContext();

        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), currentUser.getPassword())) {
            logger.error("Current password is incorrect for user: {}", currentUser.getUsername());
            throw new IllegalArgumentException("Текущий пароль неверный");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(currentUser);
        logger.info("Password updated successfully for user: {}", currentUser.getUsername());

        return currentUser.getId();
    }

    @Override
    public User getCurrentUser() {
        logger.info("Fetching current user");
        return getCurrentUserFromContext();
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        logger.info("Updating user details");

        User currentUser = getCurrentUserFromContext();

        userMapper.updateUserFromDTO(updateUser, currentUser);
        userRepository.save(currentUser);
        logger.info("User details updated successfully for user: {}", currentUser.getUsername());

        return updateUser;
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        logger.info("Updating user image");

        User currentUser = getCurrentUserFromContext();

        currentUser.setImageUrl("/images/new-avatar.jpg");
        userRepository.save(currentUser);
        logger.info("User image updated successfully for user: {}", currentUser.getUsername());
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
            logger.error("User not authorized");
            throw new UserNotAuthorizedException("Пользователь не авторизован");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found in database: {}", username);
                    return new UserNotFoundException("Пользователь не найден в базе данных");
                });
    }
}