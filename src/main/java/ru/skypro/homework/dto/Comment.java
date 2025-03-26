package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "DTO для представления комментария")
public class Comment {

    @Schema(description = "Уникальный идентификатор комментария", example = "123")
    @Min(value = 1, message = "Идентификатор комментария должен быть больше 0")
    private Integer pk;

    @Schema(description = "Текст комментария", example = "Это пример комментария")
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(min = 3, max = 500, message = "Текст комментария должен содержать от 3 до 500 символов")
    private String text;

    @Schema(description = "Идентификатор автора комментария", example = "456")
    @NotNull(message = "Идентификатор автора не может быть пустым")
    @Min(value = 1, message = "Идентификатор автора должен быть больше 0")
    private Long author;

    @Schema(description = "Имя пользователя (username) автора комментария", example = "user123")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 32, message = "Имя пользователя должно содержать от 3 до 32 символов")
    private String authorUsername;

    @Schema(description = "Ссылка на аватар автора комментария", example = "https://example.com/avatar.jpg")
    @NotBlank(message = "Ссылка на аватар не может быть пустой")
    private String authorImage;

    @Schema(description = "Время создания комментария в миллисекундах", example = "1672502400000")
    @NotNull(message = "Время создания комментария не может быть пустым")
    @Min(value = 0, message = "Время создания комментария должно быть положительным числом")
    private Long createdAt;
}