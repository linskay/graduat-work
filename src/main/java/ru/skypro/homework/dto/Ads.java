package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO для представления списка объявлений")
public class Ads {

    @NotNull(message = "Количество объявлений не может быть пустым")
    @Min(value = 0, message = "Количество объявлений не может быть отрицательным")
    @Schema(description = "Общее количество объявлений", example = "5")
    private Integer count;

    @Schema(description = "Список объявлений")
    private List<Ad> results;

    public Ads(List<Ad> results) {
        this.count = (results != null) ? results.size() : 0;
        this.results = results;
    }
}