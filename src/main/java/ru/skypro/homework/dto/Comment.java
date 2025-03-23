package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "DTO для представления комментария")
public class Comment {

    @Schema(description = "Уникальный идентификатор комментария", example = "123")
    private Integer pk;

    @Schema(description = "Текст комментария", example = "Это пример комментария")
    private String text;

    @Schema(description = "Идентификатор автора комментария", example = "456")
    private Long author;

    @Schema(description = "Имя пользователя (username) автора комментария", example = "user123")
    private String authorUsername;

    @Schema(description = "Ссылка на аватар автора комментария", example = "https://example.com/avatar.jpg")
    private String authorImage;

    @Schema(description = "Время создания комментария в миллисекундах", example = "1672502400000")
    private Long createdAt;
}