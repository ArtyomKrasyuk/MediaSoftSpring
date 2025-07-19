package com.example.MediaSoftSpring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingId implements Serializable {
    @Column(name = "visitor_id")
    private Long visitorId;
    @Column(name = "restaurant_id")
    private Long restaurantId;
}
