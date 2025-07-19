package com.example.MediaSoftSpring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class Restaurant {
    @Getter
    @AllArgsConstructor
    public enum Cuisine{
        EUROPEAN("Европейская"),
        ITALIAN("Итальянская"),
        CHINESE("Китайская");

        private final String title;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;
    @Column(nullable = false)
    private BigDecimal averageBill;
    private BigDecimal rating;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Rating> ratings;

    public Restaurant(@NonNull String title, String description, @NonNull Cuisine cuisine, @NonNull BigDecimal averageBill) {
        this.title = title;
        this.description = description;
        this.cuisine = cuisine;
        this.averageBill = averageBill;
        this.rating = BigDecimal.ZERO;
    }

    public Restaurant(){
        this.rating = BigDecimal.ZERO;
    }
}
