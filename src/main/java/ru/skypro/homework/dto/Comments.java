package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@Schema(description = "DTO для представления списка комментариев")
public class Comments {

    @Schema(description = "Общее количество комментариев", example = "5")
    private Integer count;

    @Schema(description = "Список комментариев")
    private List<Comment> results;

    public Comments(Integer count, List<Comment> results) {
        this.count = count;
        this.results = results;
    }
}