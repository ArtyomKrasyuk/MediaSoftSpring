package com.example.MediaSoftSpring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "visitor")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private int age;
    @Column(nullable = false)
    private String sex;

    @OneToMany(mappedBy = "visitor", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Rating> ratings;

    public Visitor(String name, int age, @NonNull String sex){
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
