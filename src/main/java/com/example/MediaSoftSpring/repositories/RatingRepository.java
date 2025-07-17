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
            if(elem.getId().equals(rating.getId())) return false;
        }
        ratings.add(rating);
        return true;
    }

    public boolean remove(Rating rating){
        return ratings.remove(rating);
    }

    public boolean removeById(Long id){
        return ratings.removeIf(elem -> elem.getId().equals(id));
    }

    public List<Rating> findAll(){
        return ratings;
    }

    public Rating findById(Long id){
        return ratings.stream().filter(elem -> elem.getId().equals(id)).findFirst().orElse(null);
    }
}
