package com.example.MediaSoftSpring.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Rating {
    @NonNull
    private final Long id;
    @NonNull
    private Long visitorId;
    @NonNull
    private Long restaurantId;
    private int rating;
    private String text;
}
