package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для регистрации нового пользователя")
public class Register {

    @Schema(description = "Имя пользователя (логин)", example = "user123")
    private String username;

    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;

    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "Телефон пользователя", example = "+79991234567")
    private String phone;

    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;
}