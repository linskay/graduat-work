package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;


    @Transactional
    public Long register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return null;
        }


        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(register.getUsername());
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
        userEntity.setFirstName(register.getFirstName());
        userEntity.setLastName(register.getLastName());
        userEntity.setPhone(register.getPhone());
        userEntity.setRole(register.getRole());

        userEntity = userRepository.save(userEntity);

        UserDetails userDetails = User.builder()
                .username(register.getUsername())
                .password(userEntity.getPassword())
                .roles(String.valueOf(register.getRole()))
                .build();

        jdbcUserDetailsManager.updateUser(userDetails);

        return userEntity.getId();
    }
}
