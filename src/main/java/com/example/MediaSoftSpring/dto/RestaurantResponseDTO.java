package com.example.MediaSoftSpring.dto;

import com.example.MediaSoftSpring.entities.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO с данными ресторана для вывода")
public record RestaurantResponseDTO(
        @Schema(description = "Идентификатор ресторана", example = "1752508268474")
        Long id,
        @Schema(description = "Название ресторана", example = "Итальянский ресторан")
        String title,
        @Schema(description = "Описание ресторана", example = "Ресторан с итальянской кухней")
        String description,
        @Schema(description = "Кухня ресторана")
        Restaurant.Cuisine cuisine,
        @Schema(description = "Средний счёт в ресторане", example = "10000")
        BigDecimal averageBill,
        @Schema(description = "Оценка ресторана", example = "4.5")
        BigDecimal rating
) {
}
