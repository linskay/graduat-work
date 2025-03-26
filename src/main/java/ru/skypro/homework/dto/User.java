package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для представления информации о пользователе")
public class User {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "Логин пользователя", example = "ivan@example.com")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;

    @Schema(description = "Телефон пользователя", example = "+7 (999) 123-45-67")
    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Неверный формат телефона. Пример: +7 (999) 123-45-67")
    private String phone;

    @Schema(description = "Ссылка на изображение пользователя", example = "https://example.com/image.jpg")
    private String image;
}