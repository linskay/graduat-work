package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "DTO для авторизации пользователя")
public class Login {

    @Schema(description = "Логин пользователя", example = "ivan@example.com")
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String username;

    @Schema(description = "Пароль пользователя", example = "password123")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String password;
}