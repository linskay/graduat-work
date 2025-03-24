package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exception.ErrorMessages;
import ru.skypro.homework.exception.InvalidCredentialsException;
import ru.skypro.homework.exception.UserAlreadyExistsException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.config.UserDetailsAdapter;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public boolean login(String username, String password) {
        log.debug("Попытка входа пользователя: {}", username);

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, username)));

        UserDetailsAdapter userDetails = new UserDetailsAdapter(user);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new InvalidCredentialsException(ErrorMessages.INVALID_CREDENTIALS);
        }

        log.info("Успешный вход пользователя: {}", username);
        return true;
    }

    @Override
    public Long register(Register register) {
        String email = register.getUsername();
        log.debug("Попытка регистрации пользователя: {}", email);

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(String.format(ErrorMessages.USER_ALREADY_EXISTS, email));
        }

        UserEntity user = userMapper.toUser(register);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        log.info("Пользователь успешно зарегистрирован: {}", email);

        return savedUser.getId();
    }
}