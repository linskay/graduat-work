package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для создания или обновления объявления")
public class CreateOrUpdateAdDTO {

    @Schema(description = "Заголовок объявления", example = "Продам велосипед")
    private String title;

    @Schema(description = "Цена объявления", example = "1000")
    private Integer price;

    @Schema(description = "Описание объявления", example = "Отличный велосипед, почти новый")
    private String description;
}
