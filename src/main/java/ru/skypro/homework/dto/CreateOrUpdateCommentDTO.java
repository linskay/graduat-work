package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для создания или обновления комментария")
public class CreateOrUpdateCommentDTO {

    @Schema(description = "Текст комментария", example = "Это пример текста комментария")
    private String text;
}