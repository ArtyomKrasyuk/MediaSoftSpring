package com.example.MediaSoftSpring.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO с данными оценки для вывода")
public record RatingResponseDTO(
        @Schema(description = "Идентификатор оценки", example = "1752508268474")
        // Я сделал сериализацию id в String, так как в JavaScript, который используется в Swagger UI, тип number теряет точность
        // после числа 2^53 - 1. Числа выше этого значения отображаются некорректно в Swagger UI.
        @JsonSerialize(using = ToStringSerializer.class)
        Long id,
        @Schema(description = "Идентификатор посетителя", example = "1752508268474")
        @JsonSerialize(using = ToStringSerializer.class)
        Long visitorId,
        @Schema(description = "Идентификатор ресторана", example = "1752508268474")
        @JsonSerialize(using = ToStringSerializer.class)
        Long restaurantId,
        @Schema(description = "Оценка посетителя", example = "5")
        int rating,
        @Schema(description = "Текст оценки", example = "Мне всё понравилось!")
        String text
) {
}
