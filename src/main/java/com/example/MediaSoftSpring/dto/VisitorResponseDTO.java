package com.example.MediaSoftSpring.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO с данными посетителя для вывода")
public record VisitorResponseDTO(
        @Schema(description = "Идентификатор посетителя", example = "1752508268474")
        // Я сделал сериализацию id в String, так как в JavaScript, который используется в Swagger UI, тип number теряет точность
        // после числа 2^53 - 1. Числа выше этого значения отображаются некорректно в Swagger UI.
        @JsonSerialize(using = ToStringSerializer.class)
        Long id,
        @Schema(description = "ФИО посетителя", example = "Иванов Иван Иванович")
        String name,
        @Schema(description = "Возраст посетителя")
        int age,
        @Schema(description = "Пол посетителя", allowableValues = {"Мужской", "Женский"})
        String sex
) {
}
