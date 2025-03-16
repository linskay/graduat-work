package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для изменения пароля пользователя")
public class NewPassword {

    @Schema(description = "Текущий пароль пользователя", example = "oldPassword123")
    private String currentPassword;

    @Schema(description = "Новый пароль пользователя", example = "newPassword123")
    private String newPassword;
}