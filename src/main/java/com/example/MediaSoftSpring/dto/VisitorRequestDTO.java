package com.example.MediaSoftSpring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO с данными посетителя для ввода")
public record VisitorRequestDTO(
        @Schema(description = "ФИО посетителя", example = "Иванов Иван Иванович")
        String name,
        @Min(value = 0, message = "Возраст не может быть отрицательным")
        @Max(value = 122, message = "Возраст не должен привышать 122 года")
        @Schema(description = "Возраст посетителя")
        int age,
        @Pattern(regexp = "Мужской|Женский", message = "Пол может быть 'Мужской' или 'Женский'")
        @Schema(description = "Пол посетителя", allowableValues = {"Мужской", "Женский"})
        String sex
) {
}
