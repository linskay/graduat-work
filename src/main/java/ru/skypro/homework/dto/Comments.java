package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для представления списка комментариев")
public class Comments {

    @Schema(description = "Общее количество комментариев", example = "5")
    @NotNull(message = "Количество комментариев не может быть пустым")
    @Min(value = 0, message = "Количество комментариев должно быть неотрицательным числом")
    private Integer count;

    @Schema(description = "Список комментариев")
    @NotNull(message = "Список комментариев не может быть пустым")
    private List<Comment> results;
}