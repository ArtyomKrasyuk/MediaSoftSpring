package com.example.MediaSoftSpring.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO с данными оценки для вывода")
public record RatingResponseDTO(
        @Schema(description = "Идентификатор посетителя", example = "1752508268474")
        Long visitorId,
        @Schema(description = "Идентификатор ресторана", example = "1752508268474")
        Long restaurantId,
        @Schema(description = "Оценка посетителя", example = "5")
        int rating,
        @Schema(description = "Текст оценки", example = "Мне всё понравилось!")
        String text
) {
}
