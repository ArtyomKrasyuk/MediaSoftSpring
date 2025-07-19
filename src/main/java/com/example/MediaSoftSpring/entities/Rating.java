package com.example.MediaSoftSpring.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {
    @EmbeddedId
    private RatingId id;
    private int rating;
    private String text;

    @ManyToOne
    @MapsId("visitorId")
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @ManyToOne
    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Rating(RatingId id, int rating, String text, Visitor visitor, Restaurant restaurant) {
        this.id = id;
        this.rating = rating;
        this.text = text;
        this.visitor = visitor;
        this.restaurant = restaurant;
    }
}
