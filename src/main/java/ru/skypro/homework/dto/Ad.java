package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO для представления объявления")
public class Ad {

    @NotNull(message = "Идентификатор автора не может быть пустым")
    @Schema(description = "Идентификатор автора объявления", example = "123")
    private Long author;

    @NotBlank(message = "Ссылка на изображение не может быть пустой")
    @Schema(description = "Ссылка на изображение объявления", example = "https://example.com/image.jpg")
    private String image;

    @NotNull(message = "Уникальный идентификатор объявления не может быть пустым")
    @Schema(description = "Уникальный идентификатор объявления", example = "456")
    private Integer pk;

    @NotNull(message = "Цена не может быть пустой")
    @Min(value = 0, message = "Цена должна быть больше или равна 0")
    @Max(value = 10000000, message = "Цена должна быть меньше или равна 10,000,000")
    @Schema(description = "Цена объявления", example = "1000")
    private Integer price;

    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 4, max = 32, message = "Заголовок должен содержать от 4 до 32 символов")
    @Schema(description = "Заголовок объявления", example = "Продам велосипед")
    private String title;
}