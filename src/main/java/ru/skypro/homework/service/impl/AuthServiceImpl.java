package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(UserDetailsManager manager,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public boolean login(String username, String password) {
        if (!manager.userExists(username)) {
            return false; // Пользователь не существует
        }
        UserDetails userDetails = manager.loadUserByUsername(username);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }

        manager.createUser(
                org.springframework.security.core.userdetails.User.withUsername(register.getUsername())
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .roles(register.getRole().name())
                        .build());

        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(encoder.encode(register.getPassword()));
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(register.getRole());
        user.setImageUrl("/images/default-avatar.jpg");

        userRepository.save(user);

        return true;
    }
}