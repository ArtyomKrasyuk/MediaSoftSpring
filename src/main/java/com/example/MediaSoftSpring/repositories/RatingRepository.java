package com.example.MediaSoftSpring.repositories;

import com.example.MediaSoftSpring.entities.Rating;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class RatingRepository {
    private final List<Rating> ratings;

    public RatingRepository(){
        ratings = new LinkedList<>();
    }

    public boolean save(Rating rating){
        for(Rating elem: ratings){
            if(elem.getVisitorId().equals(rating.getVisitorId()) &&
                    elem.getRestaurantId().equals(rating.getRestaurantId())){
                return false;
            }
        }
        ratings.add(rating);
        return true;
    }

    public boolean remove(Rating rating){
        return ratings.remove(rating);
    }

    public List<Rating> findAll(){
        return ratings;
    }

    public Rating findById(Long visitorId, Long restaurantId){
        for(Rating rating: ratings){
            if(rating.getVisitorId().equals(visitorId) &&
                    rating.getRestaurantId().equals(restaurantId)){
                return rating;
            }
        }
        return null;
    }
}
