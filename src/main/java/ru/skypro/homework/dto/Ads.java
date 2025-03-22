package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO для представления списка объявлений")
public class Ads {

    @Schema(description = "Общее количество объявлений", example = "5")
    private Integer count;

    @Schema(description = "Список объявлений")
    private List<Ad> results;

    public Ads(List<Ad> results) {
        this.count = results.size();
        this.results = results;
    }
}