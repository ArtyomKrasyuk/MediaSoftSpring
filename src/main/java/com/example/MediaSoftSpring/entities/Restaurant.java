package com.example.MediaSoftSpring.entities;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Restaurant {
    @Getter
    @AllArgsConstructor
    public enum Cuisine{
        EUROPEAN("Европейская"),
        ITALIAN("Итальянская"),
        CHINESE("Китайская");

        private final String title;
    }
    @NonNull
    private final Long id;
    @NonNull
    private String title;
    private String description;
    @NonNull
    private Cuisine cuisine;
    @NonNull
    private BigDecimal averageBill;
    @NonNull
    private BigDecimal rating;

    public Restaurant(@NonNull Long id, @NonNull String title, String description, @NonNull Cuisine cuisine, @NonNull BigDecimal averageBill) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cuisine = cuisine;
        this.averageBill = averageBill;
        this.rating = BigDecimal.ZERO;
    }
}
