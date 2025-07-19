package com.example.MediaSoftSpring.repositories;

import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    @Query("SELECT r from Rating r WHERE r.id.restaurantId = :restaurantId")
    List<Rating> findByRestaurantId(Long restaurantId);
}
