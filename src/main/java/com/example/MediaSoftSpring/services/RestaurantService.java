package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, RatingRepository ratingRepository) {
        this.restaurantRepository = restaurantRepository;
        this.ratingRepository = ratingRepository;
    }

    public boolean save(Restaurant restaurant){
        return restaurantRepository.save(restaurant);
    }

    public boolean remove(Restaurant restaurant){
        boolean result = restaurantRepository.remove(restaurant);
        if(result){
            removeRatings(restaurant.getId());
            return true;
        }
        else return false;
    }

    public List<Restaurant> findAll(){
        return restaurantRepository.findAll();
    }

    public Restaurant findById(Long id){
        return restaurantRepository.findById(id);
    }

    // Метод для удаления всех оценок ресторана, который был удалён
    private void removeRatings(Long id){
       List<Rating> ratings = ratingRepository.findAll().stream().filter(elem -> elem.getRestaurantId().equals(id)).toList();
       ratings.forEach(ratingRepository::remove);
    }
}
