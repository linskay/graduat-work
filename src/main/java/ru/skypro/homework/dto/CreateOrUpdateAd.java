package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Schema(description = "DTO для создания или обновления объявления")
public class CreateOrUpdateAd {

    @Schema(description = "Заголовок объявления", example = "Продам велосипед")
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 4, max = 32, message = "Заголовок должен содержать от 4 до 32 символов")
    private String title;

    @Schema(description = "Цена объявления", example = "1000")
    @NotNull(message = "Цена не может быть пустой")
    @Min(value = 0, message = "Цена должна быть не меньше 0")
    @Max(value = 10_000_000, message = "Цена должна быть не больше 10,000,000")
    private Integer price;

    @Schema(description = "Описание объявления", example = "Отличный велосипед, почти новый")
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 8, max = 64, message = "Описание должно содержать от 8 до 64 символов")
    private String description;
}
