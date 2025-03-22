package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO для изменения пароля пользователя")
public class NewPassword {

    @Schema(description = "Текущий пароль пользователя", example = "oldPassword123")
    @NotBlank(message = "Текущий пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Текущий пароль должен содержать от 8 до 16 символов")
    private String currentPassword;

    @Schema(description = "Новый пароль пользователя", example = "newPassword123")
    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Новый пароль должен содержать от 8 до 16 символов")
    private String newPassword;
}