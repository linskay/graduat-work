package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для представления информации о приложении")
public class AppInfoDTO {

    @Schema(description = "Название приложения", example = "My Application")
    private String name;

    @Schema(description = "Описание приложения", example = "Это приложение для управления объявлениями")
    private String description;

    @Schema(description = "Версия приложения", example = "1.0.0")
    private String version;
}