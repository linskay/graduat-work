package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для авторизации пользователя")
public class Login {

    @Schema(description = "Логин пользователя (email)", example = "ivan@example.com")
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String username;

    @Schema(description = "Пароль пользователя", example = "password123")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}