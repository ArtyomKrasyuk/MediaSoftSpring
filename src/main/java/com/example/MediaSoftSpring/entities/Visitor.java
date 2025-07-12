package com.example.MediaSoftSpring.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Visitor {
    @NonNull
    private final Long id;
    private String name;
    private int age;
    @NonNull
    private String sex;
}
