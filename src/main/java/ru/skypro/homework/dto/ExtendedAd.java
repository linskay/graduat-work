package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "DTO для представления расширенной информации об объявлении")
public class ExtendedAd extends Ad {

    @Schema(description = "Имя автора объявления", example = "Олег")
    @NotBlank(message = "Имя автора не может быть пустым")
    @Size(min = 2, max = 32, message = "Имя автора должно содержать от 2 до 32 символов")
    private String authorFirstName;

    @Schema(description = "Фамилия автора объявления", example = "Олегович")
    @NotBlank(message = "Фамилия автора не может быть пустой")
    @Size(min = 2, max = 32, message = "Фамилия автора должна содержать от 2 до 32 символов")
    private String authorLastName;

    @Schema(description = "Описание объявления", example = "Отличный товар, почти новый")
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 8, max = 512, message = "Описание должно содержать от 8 до 512 символов")
    private String description;

    @Schema(description = "Логин автора объявления", example = "ivan@example.com")
    @NotBlank(message = "Логин автора не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Некорректный формат email")
    private String email;

    @Schema(description = "Телефон автора объявления", example = "+79991234567")
    @NotBlank(message = "Телефон автора не может быть пустым")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Неверный формат телефона. Пример: +7 (999) 123-45-67")
    private String phone;
}