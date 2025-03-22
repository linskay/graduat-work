package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JdbcTemplate jdbcTemplate;
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public boolean login(String username, String password) {
        log.debug("Попытка входа пользователя: {}", username);

        try {
            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                log.info("Успешный вход пользователя: {}", username);
                return true;
            }

            log.warn("Неверный пароль для пользователя: {}", username);
            return false;
        } catch (UsernameNotFoundException e) {
            log.error("Пользователь не найден: {}", username);
            return false;
        } catch (Exception e) {
            log.error("Ошибка при входе пользователя: {}", username, e);
            return false;
        }
    }

    @Override
    public boolean register(Register register) {
        String email = register.getUsername();
        log.debug("Попытка регистрации пользователя: {}", email);

        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Пользователь с email {} уже существует", email);
            return false;
        }

        try {
            log.info("Регистрация нового пользователя: {}", register);

            jdbcTemplate.update(
                    "INSERT INTO users (email, password, enabled, first_name, last_name, phone, role) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    email,
                    passwordEncoder.encode(register.getPassword()),
                    true,
                    register.getFirstName(),
                    register.getLastName(),
                    register.getPhone(),
                    register.getRole().name()
            );

            jdbcTemplate.update(
                    "INSERT INTO authorities (email, authority) VALUES (?, ?)",
                    email,
                    "ROLE_" + register.getRole().name()
            );

            log.info("Пользователь успешно зарегистрирован: {}", email);
            return true;
        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя: {}", email, e);
            return false;
        }
    }
}