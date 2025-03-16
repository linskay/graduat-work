package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO для представления списка объявлений")
public class AdsDTO {

    @Schema(description = "Общее количество объявлений", example = "5")
    private Integer count;

    @Schema(description = "Список объявлений")
    private List<AdDTO> results;
}