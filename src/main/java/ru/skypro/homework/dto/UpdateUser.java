package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Schema(description = "DTO для обновления информации о пользователе")
public class UpdateUser {

    @Schema(description = "Имя пользователя", example = "Олег")
    @Size(min = 3, max = 10, message = "Имя должно содержать от 3 до 10 символов")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Олегович")
    @Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов")
    private String lastName;

    @Schema(description = "Телефон пользователя", example = "+7 (999) 123-45-67")
    @Pattern(
            regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Неверный формат телефона. Пример: +7 (999) 123-45-67")
    private String phone;
}