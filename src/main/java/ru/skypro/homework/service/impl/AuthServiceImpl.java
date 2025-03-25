package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JdbcTemplate jdbcTemplate; // Добавили JdbcTemplate
    private final JdbcUserDetailsManager jdbcUserDetailsManager;



    @Transactional
    public Long register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return null; // Или можно выбросить исключение
        }

        // 1. Создаем UserEntity
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(register.getUsername());
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
        userEntity.setFirstName(register.getFirstName());
        userEntity.setLastName(register.getLastName());
        userEntity.setPhone(register.getPhone());
        userEntity.setRole(register.getRole());

        // 2. Сохраняем UserEntity в базе данных
        userEntity = userRepository.save(userEntity);

        // 3. Создаем UserDetails (только для Spring Security)
        UserDetails userDetails = User.builder()
                .username(register.getUsername())
                .password(userEntity.getPassword()) // Используем хешированный пароль из UserEntity
                .roles(String.valueOf(register.getRole()))
                .build();

        // 4. Создаем authority в JdbcUserDetailsManager (только для authorities)
        jdbcUserDetailsManager.updateUser(userDetails);

        // 5. Возвращаем ID созданного UserEntity
        return userEntity.getId();
    }


//
//    public boolean register(Register register) {
//        // Используем UserRepository для проверки существования пользователя
//        if (userRepository.existsByEmail(register.getUsername())) {
//            return false;
//        }
//
//        UserDetails user = User.builder()
//                .username(register.getUsername())
//                .password(passwordEncoder.encode(register.getPassword()))
//                .roles(register.getRole().name())
//                .build();
//
//        jdbcUserDetailsManager.createUser(user);
//        return true;
//    }
//    @Override
//    public Long register(Register register) {
//        String email = register.getUsername();
//
//        if (userRepository.existsByEmail(email)) {
//            return null; // Или выбросить исключение, если хотите
//        }
//
//        UserEntity user = userMapper.toUser(register);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        UserEntity savedUser = userRepository.save(user);
//
//        // Создание Authority с использованием JdbcTemplate
//        String sql = "INSERT INTO authorities (username, authority) VALUES (?, ?)";
//        jdbcTemplate.update(sql, email, "ROLE_" + user.getRole().name());
//
//        return savedUser.getId();
//    }


    @Override
    public boolean login(String username, String password) {
        log.debug("Попытка входа пользователя: {}", username);
//
//        UserEntity user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, username)));
//
//        UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
//
//        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
//            throw new InvalidCredentialsException(ErrorMessages.INVALID_CREDENTIALS);
//        }
//
//        log.info("Успешный вход пользователя: {}", username);
        return true;
    }}

//    @Override
//    public Long register(Register register) {
//        String email = register.getUsername();
//        log.debug("Попытка регистрации пользователя: {}", email);
//
//        if (userRepository.existsByEmail(email)) {
//            throw new UserAlreadyExistsException(String.format(ErrorMessages.USER_ALREADY_EXISTS, email));
//        }
//
//        UserEntity user = userMapper.toUser(register);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        UserEntity savedUser = userRepository.save(user);
//        log.info("Пользователь успешно зарегистрирован: {}", email);
//
//        return savedUser.getId();
//    }
