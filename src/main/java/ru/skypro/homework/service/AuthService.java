package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

public interface AuthService {
    boolean login(String userName, String password);

    Long register(Register register);
}
