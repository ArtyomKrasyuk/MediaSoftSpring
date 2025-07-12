package com.example.MediaSoftSpring.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Rating {
    @NonNull
    private final Long visitorId;
    @NonNull
    private final Long restaurantId;
    private final int rating;
    private String text;
}
