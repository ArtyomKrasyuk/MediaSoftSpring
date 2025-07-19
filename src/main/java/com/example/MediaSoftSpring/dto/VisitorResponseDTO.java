package com.example.MediaSoftSpring.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO с данными посетителя для вывода")
public record VisitorResponseDTO(
        @Schema(description = "Идентификатор посетителя", example = "1752508268474")
        Long id,
        @Schema(description = "ФИО посетителя", example = "Иванов Иван Иванович")
        String name,
        @Schema(description = "Возраст посетителя")
        int age,
        @Schema(description = "Пол посетителя", allowableValues = {"Мужской", "Женский"})
        String sex
) {
}
