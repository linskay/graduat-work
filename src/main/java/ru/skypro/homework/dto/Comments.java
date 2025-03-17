package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO для представления списка комментариев")
public class Comments {

    @Schema(description = "Общее количество комментариев", example = "5")
    private Integer count;

    @Schema(description = "Список комментариев")
    private List<Comment> results;
}