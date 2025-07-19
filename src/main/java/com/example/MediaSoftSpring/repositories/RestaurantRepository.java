package com.example.MediaSoftSpring.repositories;

import com.example.MediaSoftSpring.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r WHERE r.rating >= :rating")
    List<Restaurant> findByRating(BigDecimal rating);
}
