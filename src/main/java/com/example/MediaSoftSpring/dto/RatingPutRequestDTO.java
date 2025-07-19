package com.example.MediaSoftSpring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "DTO с данными оценки для обновления")
public record RatingPutRequestDTO(
    @Min(value = 1, message = "Минимальная оценка — 1")
    @Max(value = 5, message = "Максимальная оценка — 5")
    @Schema(description = "Оценка посетителя", example = "5")
    int rating,
    @Schema(description = "Текст оценки", example = "Мне всё понравилось!")
    String text
) {
}
