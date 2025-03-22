package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "DTO для представления расширенной информации об объявлении")
public class ExtendedAd extends Ad {

    @Schema(description = "Имя автора объявления", example = "Олег")
    private String authorFirstName;

    @Schema(description = "Фамилия автора объявления", example = "Олегович")
    private String authorLastName;

    @Schema(description = "Описание объявления", example = "Отличный товар, почти новый")
    private String description;

    @Schema(description = "Логин автора объявления", example = "ivan@example.com")
    private String email;

    @Schema(description = "Телефон автора объявления", example = "+79991234567")
    private String phone;
}