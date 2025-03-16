package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO для представления информации о пользователе")
public class UserDTO {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "Email пользователя", example = "ivan@example.com")
    private String email;

    @Schema(description = "Телефон пользователя", example = "+79991234567")
    private String phone;

    @Schema(description = "Ссылка на изображение пользователя", example = "https://example.com/image.jpg")
    private String imageUrl;
}