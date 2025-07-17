package com.example.MediaSoftSpring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

@Schema(description = "DTO с данными ресторана для ввода")
public record RestaurantRequestDTO(
        @NotBlank(message = "Название ресторана не должно быть пустым")
        @Schema(description = "Название ресторана", example = "Итальянский ресторан")
        String title,
        @Schema(description = "Описание ресторана", example = "Ресторан с итальянской кухней")
        String description,
        @Pattern(regexp = "EUROPEAN|ITALIAN|CHINESE", message = "Кухня должна быть 'EUROPEAN', 'ITALIAN', или 'CHINESE'")
        @Schema(description = "Кухня ресторана")
        String cuisine,
        @Min(value = 0, message = "Средний счёт не может быть отрицательным")
        @Schema(description = "Средний счёт в ресторане", example = "10000")
        BigDecimal averageBill
) {
}
