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

    @Schema(description = "Идентификатор автора объявления", example = "123")
    private Long author;

    @Schema(description = "Ссылка на изображение объявления", example = "https://example.com/image.jpg")
    private String image;

    @Schema(description = "Уникальный идентификатор объявления", example = "456")
    private Integer pk;

    @NotNull
    @Min(0)
    @Max(10000000)
    @Schema(description = "Цена объявления", example = "1000")
    private Integer price;

    @NotBlank
    @Size(min = 4, max = 32)
    @Schema(description = "Заголовок объявления", example = "Продам велосипед")
    private String title;
}