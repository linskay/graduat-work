package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для регистрации нового пользователя")
public class Register {

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

    @Schema(description = "Имя пользователя", example = "Олег")
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 16, message = "Имя должно содержать от 2 до 16 символов")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Олегов")
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 16, message = "Фамилия должна содержать от 2 до 16 символов")
    private String lastName;

    @Schema(description = "Телефон пользователя", example = "+7 (999) 123-45-67")
    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Неверный формат телефона. Пример: +7 (999) 123-45-67")
    private String phone;

    @Schema(description = "Роль пользователя", example = "USER")
    @NotNull(message = "Роль не может быть пустой")
    private Role role;
}