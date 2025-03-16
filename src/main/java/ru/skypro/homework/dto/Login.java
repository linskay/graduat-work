package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для авторизации пользователя")
public class Login {

    @Schema(description = "Имя пользователя (логин)", example = "user123")
    private String username;

    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;
}