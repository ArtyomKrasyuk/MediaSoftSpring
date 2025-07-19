package com.example.MediaSoftSpring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "DTO с данными оценки для сохранения")
public record RatingPostRequestDTO(
        @Min(value = 1, message = "Идентификатор пользователя должен быть не меньше 1")
        @Schema(description = "Идентификатор посетителя", example = "1752508268474")
        Long visitorId,
        @Min(value = 1, message = "Идентификатор ресторана должен быть не меньше 1")
        @Schema(description = "Идентификатор ресторана", example = "1752508268474")
        Long restaurantId,
        @Min(value = 1, message = "Минимальная оценка — 1")
        @Max(value = 5, message = "Максимальная оценка — 5")
        @Schema(description = "Оценка посетителя", example = "5")
        int rating,
        @Schema(description = "Текст оценки", example = "Мне всё понравилось!")
        String text
) {
}
