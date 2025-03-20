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
        try {
            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                log.info("User logged in successfully: {}", username);
                return true;
            } else {
                log.warn("Invalid password for user: {}", username);
                return false;
            }
        } catch (UsernameNotFoundException e) {
            log.error("User not found: {}", username);
            return false;
        } catch (Exception e) {
            log.error("Login failed for user: {}", username, e);
            return false;
        }
    }

    @Override
    public boolean register(Register register) {
        String email = register.getUsername();
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("User already exists: {}", email);
            return false;
        }

        try {
            log.info("Registering user with data: {}", register);

            jdbcTemplate.update(
                    "INSERT INTO users (email, password, enabled, first_name, last_name, phone, role) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    register.getUsername(),
                    passwordEncoder.encode(register.getPassword()),
                    true,
                    register.getFirstName(),
                    register.getLastName(),
                    register.getPhone(),
                    register.getRole().name()
            );

            jdbcTemplate.update(
                    "INSERT INTO authorities (email, authority) VALUES (?, ?)",
                    register.getUsername(),
                    "ROLE_" + register.getRole().name()
            );

            log.info("User registered successfully: {}", register.getUsername());
            return true;
        } catch (Exception e) {
            log.error("Failed to register user: {}", register.getUsername(), e);
            return false;
        }
    }
}